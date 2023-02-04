package com.example.demo.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.Enum.ERoles;
import com.example.demo.Entity.Response.ResponseClass;
import com.example.demo.Entity.Role;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Security.Service.JWTokenService;
import com.example.demo.Singleton.SingletonOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.Security.SecurityConstants.*;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleService roleService;
    @Autowired
    ValidationService validationService;
    @Autowired
    SendingSMS sendingSMS;
    @Autowired
    JWTokenService jwTokenService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Добавление пользователя
     * @param body [json] поля сущности UserEntity
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - User with this telephoneNumber already exist
     * @code 400 - Incorrect validation (ValidationService.class)
     */
    public void addUser(String body) {

        String fio = StaticMethods.parsingJson(body, "FIO");
        String password = StaticMethods.parsingJson(body, "password");
        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");

        if(fio == null || password == null || telephoneNumber == null)
            return;

        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
        if(userEntity != null && userEntity.getTimeOfCreation() == null) {
            StaticMethods.createResponse(400,"User with this telephoneNumber already exist");
            return;
        }
        if(userEntity == null)
            userEntity = new UserEntity();

        List<Role> role = new ArrayList<>();
        role.add(roleService.findByRole(ERoles.valueOf("USER")));
        userEntity.setTelephoneNumber(telephoneNumber);
        userEntity.setRoles(role);
        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        userEntity.setFIO(fio);
        userEntity.setTimeOfCreation(System.currentTimeMillis());

        // ------Реальный рандомный код. Теперь 111111, тк рассылка отключена (См. ниже) ----------------------------------------!!!!!!!!!!!!!!!!!!!
//        userEntity.setCode((int) (Math.random() * 1_000_000));
        userEntity.setCode(111111);

        userEntity.setIsMan(null);
        userEntity.setVerification(false);
        userRepository.save(userEntity);

        // ------------Отправка кода через сообщение. Отключено, ибо списывает деньги)) -------------------------------------------------------!!!!!!!!!!!!!!!!
//        sendingSMS.createSMS(userEntity.getTelephoneNumber(), String.valueOf(userEntity.getCode()));

        StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "Created");
    }


    /**
     * Удаление пользователя по полю :telephoneNumber
     * @param telephoneNumber :telephoneNumber Пользователя
     */
    public void deleteByTelephoneNumber(String telephoneNumber) {
        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
        userRepository.delete(userEntity);
    }


    /**
     * Поиск Пользователя по полю :telephoneNumber
     * @param telephoneNumber :telephoneNumber Пользователя
     */
    public UserEntity findByTelephoneNumber(String telephoneNumber){
        return userRepository.findByTelephoneNumber(telephoneNumber);
    }


    /**
     * Проверка роли пользователя
     * @param request request, в котором должен быть jwToken
     * @code 200 - ResponseEntity<ResponseClass>
     * @code 400 - Incorrect JWT token
     */
    public ResponseEntity<ResponseClass> checkRole(HttpServletRequest request) {

        String tokenWithPrefix = request.getHeader(HEADER_JWT_STRING);
        if(tokenWithPrefix!=null && tokenWithPrefix.startsWith(TOKEN_PREFIX)){
            String token = tokenWithPrefix.replace(TOKEN_PREFIX,"");
            try{
                String role = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getClaim("role").asString();

                return ResponseEntity.ok(new ResponseClass(200, role, request.getServletPath()));
            }catch (IllegalArgumentException | JWTDecodeException ignored){}

        }


        return ResponseEntity.status(400).body(new ResponseClass(400, "Incorrect JWT token", request.getServletPath()));

    }


    /**
     * Подтверждение кода, который пришёл по СМС на указанный при регистрации телефонный номер
     * @param body [json]:
     *             telephoneNumber - телефонный номер;
     *             code - код.
     * @code 201 - Code is confirmed
     * @code 400 - Incorrect JSON
     * @code 400 - User with this :telephoneNumber doesn't exist
     * @code 400 - Incorrect code
     * @code 400 - Code has already been confirmed
     */
    public void codeConfirmation(String body) {

        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");
        String code = StaticMethods.parsingJson(body, "code");
        if(code == null || telephoneNumber == null)
            return;

        synchronized (SingletonOne.getSingleton()) {
            UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
            if (userEntity == null) {
                StaticMethods.createResponse(400, "User with this :telephoneNumber doesn't exist");
                return;
            }

            if (userEntity.getCode() != null && userEntity.getCode() == Integer.parseInt(code)) {
                userEntity.setCode(null);
                userEntity.setTimeOfCreation(null);
                userEntity.setVerification(true);
                userRepository.save(userEntity);
                StaticMethods.createResponse(201, "Code is confirmed");
            } else {
                if (userEntity.getCode() == null)
                    StaticMethods.createResponse(400, "Code has already been confirmed");
                else
                    StaticMethods.createResponse(400, "Incorrect code");
            }
        }

    }

    public void sendSMSForPasswordRecovery(String body) {

        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");
        if(telephoneNumber == null)
            return;

        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
        if(userEntity == null || !userEntity.getVerification()){
            StaticMethods.createResponse(400, "User with this :telephoneNumber doesn't exist");
            return;
        }

        userEntity.setTimeOfCreation(System.currentTimeMillis());
        userEntity.setCode((int) (Math.random() * 1_000_000));
        sendingSMS.createSMS(telephoneNumber, String.valueOf(userEntity.getCode()));
        StaticMethods.createResponse(201, "Code has been sent");
    }

    public void changePassword(String body) {

        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");
        String newPassword = StaticMethods.parsingJson(body, "password");
        if(newPassword == null || telephoneNumber == null)
            return;

        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
        if(userEntity == null){
            StaticMethods.createResponse(400, "User with this :telephoneNumber doesn't exist");
            return;
        }

        userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userRepository.save(userEntity);
        StaticMethods.createResponse(201, "Password have changed");

    }

    public List<UserEntity> findAll(){
        return userRepository.findAll();
    }

    public void delete(UserEntity userEntity){
        userRepository.delete(userEntity);
    }

    public Boolean checkPassword(String password, HttpServletRequest request) {
        String telephoneNumber = jwTokenService.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null)
            return false;

        return bCryptPasswordEncoder.matches(password, userEntity.getPassword());
    }


    public void changeGender(String gender, HttpServletRequest request) {
        String telephoneNumber = jwTokenService.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");
        }

        userEntity.setIsMan(Boolean.valueOf(gender));
        userRepository.save(userEntity);
        StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "Gender was changing");
    }

    public void changeFio(String fio, HttpServletRequest request) {
        String telephoneNumber = jwTokenService.
                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
        if(userEntity == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");

        userEntity.setFIO(fio);
        userRepository.save(userEntity);
        StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "FIO has been changed");
    }
}
