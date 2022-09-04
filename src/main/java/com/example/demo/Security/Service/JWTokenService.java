package com.example.demo.Security.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Security.Entity.RefreshToken;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

import static com.example.demo.Security.SecurityConstants.*;

@Service
public class JWTokenService {

    /** Получаем имя из токена, который нельзя верифицировать, ибо он просрочен */
    public String decodeJWT(String jwt){
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String json = new String(decoder.decode(chunks[1]));

        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Создаём JWT */
    public String createJWT(String telephoneNumber, String role){
        return JWT.create()
                .withSubject(telephoneNumber)
                .withClaim("role", role)
                .withExpiresAt(new Date(System.currentTimeMillis()+ EXPIRATION_TIME_OF_JWT))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));
    }

    /** Получение telephoneNumber из JWT */
    public String getNameFromJWT(String token){
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                .build()
                .verify(token)
                .getSubject();
    }

    /** Получение роли из JWT */
    public String getRoleFromJWT(String token){
        return JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                        .build()
                        .verify(token)
                        .getClaim("role").asString();
    }




}
