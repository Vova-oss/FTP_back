package com.example.demo.Security.Controller;

import com.example.demo.Controller.AuxiliaryClasses.StaticMethods;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Security.Entity.RefreshToken;
import com.example.demo.Security.Service.JWTokenService;
import com.example.demo.Security.Service.RefreshTokenService;
import com.example.demo.Service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Base64;

import static com.example.demo.Security.SecurityConstants.*;

@Controller
@Api(tags = "Security")
@CrossOrigin("http://localhost:3000")
public class SecurityController {

    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    JWTokenService jwTokenService;
    @Autowired
    UserService userService;


    @ApiOperation(value = "Обновление токенов (В хедере необходимы два токена с именами ExpiredJWT и RefreshToken")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "---"),
            @ApiResponse(code = 432, message = "Refresh token doesn't exist"),
            @ApiResponse(code = 433, message = "Refresh token was expired")
    })
    @PostMapping("/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response){
        refreshTokenService.refreshToken(request, response);
    }
}
