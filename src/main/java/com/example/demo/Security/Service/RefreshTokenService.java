package com.example.demo.Security.Service;

import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Security.Entity.RefreshToken;
import com.example.demo.Security.Entity.RefreshTokenRepository;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.demo.Security.SecurityConstants.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserService userService;
    @Autowired
    JWTokenService jwTokenService;

    /** Создание нового refresh-токена */
    public RefreshToken createRTbyUserTelephoneNumber(String telephoneNumber){
        UserEntity userEntity = userService.findByTelephoneNumber(telephoneNumber);

        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUserEntity(userEntity);
        refreshToken.setExpiryDate(Instant.now().plusMillis(EXPIRATION_TIME_OF_RT));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    /** Нахождение объекта refreshToken в БД, через его поле token */
    public Optional<RefreshToken> findByToken(String refreshToken){
        return refreshTokenRepository.findByToken(refreshToken);
    }


    /** Верификация refresh-токена */
    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(refreshToken);
            return null;
        }

        // Обновляю время просрочки и ставлю новый токен
        refreshToken.setExpiryDate(Instant.now().plusMillis(EXPIRATION_TIME_OF_RT));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }


    /** Удаление всех токенов, принадлежащих конкретному пользователю */
    public void deleteAllByUser(UserEntity userEntity) {
        List<RefreshToken> list = refreshTokenRepository.findAllByUserEntity(userEntity);
        refreshTokenRepository.deleteAll(list);
    }


    /**
     * Обновление refresh-токена и jwt-токена (от клиента нужны они оба. В ответе в хедер запихнутся 2 новых)
     * @code 201 - Created
     * @code 432 - Refresh token doesn't exist
     * @code 433 - Refresh token was expired
     * */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){

        String rToken = request.getHeader(HEADER_RT_STRING);
        String jwToken = request.getHeader(HEADER_EXPIRED_JWT_STRING);
        if(rToken!=null && jwToken!=null){
            RefreshToken refreshToken = findByToken(rToken).orElse(null);
            if(refreshToken == null){
                StaticMethods.createResponse(request, response,
                        432,"Refresh token doesn't exist");
                String telephoneNumber = jwTokenService.decodeJWT(jwToken);
                UserEntity userEntity = userService.findByTelephoneNumber(telephoneNumber);
                if(userEntity!=null){
                    deleteAllByUser(userEntity);
                }
                return;
            }
            refreshToken = verifyExpiration(refreshToken);
            if(refreshToken == null){
                StaticMethods.createResponse(request, response,
                        433,"Refresh token was expired");
                return;
            }

            UserEntity userEntity = refreshToken.getUserEntity();

            String token = jwTokenService.createJWT(userEntity.getTelephoneNumber(), String.valueOf(userEntity.getRoles().get(0).getRole()));
            response.addHeader(HEADER_JWT_STRING, TOKEN_PREFIX+token);
            response.addHeader(HEADER_RT_STRING, refreshToken.getToken());

            //Устанавливаем, какие хедеры может видеть фронт
            response.addHeader("Access-Control-Expose-Headers", HEADER_JWT_STRING+","+HEADER_RT_STRING);

            StaticMethods.createResponse(request, response,
                    HttpServletResponse.SC_CREATED, "Created");

        }

    }

}
