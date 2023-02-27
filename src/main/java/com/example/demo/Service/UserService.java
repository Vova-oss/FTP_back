package com.example.demo.Service;

import com.example.demo.Controller.AuxiliaryClasses.CustomException;
import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.Roles_Users;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repositories.Roles_UsersRepo;
import com.example.demo.Repositories.UserRepo;
import com.example.demo.Singleton.SingletonOne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;
    @Autowired
    Roles_UsersRepo roles_usersRepo;

//    @Autowired
//    RoleService roleService;
//    @Autowired
//    ValidationService validationService;
//    @Autowired
//    SendingSMS sendingSMS;
//    @Autowired
//    JWTokenService jwTokenService;
//
//    @Autowired
//    BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Добавление пользователя
     * @param body [json] поля сущности UserEntity
     * @code 201 - Created
     * @code 400 - Incorrect JSON
     * @code 400 - User with this telephoneNumber already exist
     * @code 400 - Incorrect validation (ValidationService.class)
     */
    public Mono<Void> addUser(String body) {
        String fio = StaticMethods.parsingJson(body, "FIO");
        String password = StaticMethods.parsingJson(body, "password");
        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");

        if(fio == null || password == null || telephoneNumber == null)
            return Mono.error(() -> new CustomException("Some of the required fields are empty"));

        return userRepo
                .findByTelephoneNumber(telephoneNumber)
                .switchIfEmpty(
                        Mono.just(createUserForAdd(null, telephoneNumber, password, fio))
                )
                .flatMap(userEntity -> {
                    if(userEntity != null && userEntity.getTimeOfCreation() == null)
                        return Mono.error(new CustomException("User with this telephoneNumber already exist"));
                    userEntity = createUserForAdd(userEntity, telephoneNumber, password, fio);
                    return userRepo.save(userEntity);
                })
                .flatMap(userEntity -> {
                    return roles_usersRepo.addRelation(userEntity.getId(), 2L)
                            .onErrorComplete();
                })
                .then(Mono.empty());
    }

    private UserEntity createUserForAdd(UserEntity userEntity, String telephoneNumber, String password, String fio){
        if(userEntity == null)
            userEntity = new UserEntity();
        userEntity.setTelephoneNumber(telephoneNumber);
        // ----------------- Нужно добавить кодирование при добавление Security --------------------------------------------- !!!!!!!!!!!!!!!!!!!
//        userEntity.setPassword(bCryptPasswordEncoder.encode(password));
        userEntity.setPassword(password);
        userEntity.setFIO(fio);
        userEntity.setTimeOfCreation(System.currentTimeMillis());

        // ------Реальный рандомный код. Теперь 111111, тк рассылка отключена (См. ниже) ----------------------------------------!!!!!!!!!!!!!!!!!!!
//        userEntity.setCode((int) (Math.random() * 1_000_000));
        userEntity.setCode(111111);

        userEntity.setIsMan(null);
        userEntity.setVerification(false);
        return userEntity;
    }

//
//
//    /**
//     * Удаление пользователя по полю :telephoneNumber
//     * @param telephoneNumber :telephoneNumber Пользователя
//     */
//    public void deleteByTelephoneNumber(String telephoneNumber) {
//        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
//        userRepository.delete(userEntity);
//    }
//
//
//    /**
//     * Поиск Пользователя по полю :telephoneNumber
//     * @param telephoneNumber :telephoneNumber Пользователя
//     */
//    public UserEntity findByTelephoneNumber(String telephoneNumber){
//        return userRepository.findByTelephoneNumber(telephoneNumber);
//    }
//
//
//    /**
//     * Проверка роли пользователя
//     * @param request request, в котором должен быть jwToken
//     * @code 200 - ResponseEntity<ResponseClass>
//     * @code 400 - Incorrect JWT token
//     */
//    public ResponseEntity<ResponseClass> checkRole(HttpServletRequest request) {
//
//        String tokenWithPrefix = request.getHeader(HEADER_JWT_STRING);
//        if(tokenWithPrefix!=null && tokenWithPrefix.startsWith(TOKEN_PREFIX)){
//            String token = tokenWithPrefix.replace(TOKEN_PREFIX,"");
//            try{
//                String role = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
//                        .build()
//                        .verify(token)
//                        .getClaim("role").asString();
//
//                return ResponseEntity.ok(new ResponseClass(200, role, request.getServletPath()));
//            }catch (IllegalArgumentException | JWTDecodeException ignored){}
//
//        }
//
//
//        return ResponseEntity.status(400).body(new ResponseClass(400, "Incorrect JWT token", request.getServletPath()));
//
//    }


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
    public Mono<Void> codeConfirmation(String body) {
        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");
        String code = StaticMethods.parsingJson(body, "code");
        if(code == null || telephoneNumber == null)
            return Mono.error(() -> new CustomException("Some of the required fields are empty"));

        return userRepo
                .findByTelephoneNumber(telephoneNumber)
                .switchIfEmpty(Mono.error(() -> new CustomException("User with this :telephoneNumber doesn't exist")))
                .flatMap(userEntity -> {
                    if (userEntity.getCode() != null && userEntity.getCode() == Integer.parseInt(code)) {
                        userEntity.setCode(null);
                        userEntity.setTimeOfCreation(null);
                        userEntity.setVerification(true);
                        return userRepo.save(userEntity);
                    } else {
                        if (userEntity.getCode() == null)
                            return Mono.error(() -> new CustomException("Code has already been confirmed"));
                        else
                            return Mono.error(() -> new CustomException("Incorrect code"));
                    }
                })
                .then(Mono.empty());

//
//
//
//
//        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");
//        String code = StaticMethods.parsingJson(body, "code");
//        if(code == null || telephoneNumber == null)
//            return;
//
//        synchronized (SingletonOne.getSingleton()) {
//            UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
//            if (userEntity == null) {
//                StaticMethods.createResponse(400, "User with this :telephoneNumber doesn't exist");
//                return;
//            }
//
//            if (userEntity.getCode() != null && userEntity.getCode() == Integer.parseInt(code)) {
//                userEntity.setCode(null);
//                userEntity.setTimeOfCreation(null);
//                userEntity.setVerification(true);
//                userRepository.save(userEntity);
//                StaticMethods.createResponse(201, "Code is confirmed");
//            } else {
//                if (userEntity.getCode() == null)
//                    StaticMethods.createResponse(400, "Code has already been confirmed");
//                else
//                    StaticMethods.createResponse(400, "Incorrect code");
//            }
//        }
    }

//    public void sendSMSForPasswordRecovery(String body) {
//
//        String telephoneNumber = StaticMethods.parsingJson(body, "telephoneNumber");
//        if(telephoneNumber == null)
//            return;
//
//        UserEntity userEntity = findByTelephoneNumber(telephoneNumber);
//        if(userEntity == null || !userEntity.getVerification()){
//            StaticMethods.createResponse(400, "User with this :telephoneNumber doesn't exist");
//            return;
//        }
//
//        userEntity.setTimeOfCreation(System.currentTimeMillis());
//        userEntity.setCode((int) (Math.random() * 1_000_000));
//        sendingSMS.createSMS(telephoneNumber, String.valueOf(userEntity.getCode()));
//        StaticMethods.createResponse(201, "Code has been sent");
//    }
//
//    public void changePassword(String body, HttpServletRequest request) {
//
//        String oldPassword = StaticMethods.parsingJson(body, "oldPassword");
//        String newPassword = StaticMethods.parsingJson(body, "newPassword");
//
//        String telephoneNumber = jwTokenService.
//                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
//        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
//        if(userEntity == null)
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");
//
//        if (!bCryptPasswordEncoder.matches(oldPassword, userEntity.getPassword()))
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect current password");
//
//        userEntity.setPassword(bCryptPasswordEncoder.encode(newPassword));
//        userRepository.save(userEntity);
//        StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "Password have changed");
//
//    }
//
//    public List<UserEntity> findAll(){
//        return userRepository.findAll();
//    }
//
//    public void delete(UserEntity userEntity){
//        userRepository.delete(userEntity);
//    }
//
//    public Boolean checkPassword(String password, HttpServletRequest request) {
//        String telephoneNumber = jwTokenService.
//                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
//        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
//        if(userEntity == null)
//            return false;
//
//        return bCryptPasswordEncoder.matches(password, userEntity.getPassword());
//    }
//
//
//    public void changeGender(String gender, HttpServletRequest request) {
//        String telephoneNumber = jwTokenService.
//                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
//        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
//        if(userEntity == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");
//        }
//
//        userEntity.setIsMan(Boolean.valueOf(gender));
//        userRepository.save(userEntity);
//        StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "Gender was changing");
//    }
//
//    public void changeFio(String fio, HttpServletRequest request) {
//        String telephoneNumber = jwTokenService.
//                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
//        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
//        if(userEntity == null)
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect jwt-token");
//
//        userEntity.setFIO(fio);
//        userRepository.save(userEntity);
//        StaticMethods.createResponse(HttpServletResponse.SC_CREATED, "FIO has been changed");
//    }
//
//    public UserDTO getInfoAboutUser(HttpServletRequest request) {
//        String telephoneNumber = jwTokenService.
//                getNameFromJWT(request.getHeader(HEADER_JWT_STRING).replace(TOKEN_PREFIX,""));
//        UserEntity userEntity = userRepository.findByTelephoneNumber(telephoneNumber);
//        return UserDTO.createUserDTO(userEntity);
//    }
}
