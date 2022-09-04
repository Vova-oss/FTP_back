package com.example.demo;

import com.example.demo.Entity.Response.ResponseClass;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Service.ValidationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.File;
import java.util.Base64;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    ValidationService validationService;

    @Test
    void validationUserEntity(HttpServletRequest request, HttpServletResponse  response){
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword(" ");
        userEntity.setTelephoneNumber("");
        validationService.validation(request, response,userEntity);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ResponseClass> ExceptionOfTokenExpired(HttpServletRequest request){
        System.out.println("SOMETHING WRONG");
        return ResponseEntity.status(400).body(new ResponseClass(400, "Incorrect JSON (EX)", request.getServletPath()));
    }

    @Test
    void decodeJWT(){
        String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmaXJzdEBtYWlsLnJ1Iiwicm9sZSI6IlVTRVIiLCJleHAiOjE2MjgwOTk2NDJ9.zYeSkwglf__GmvqfPy3sbfZuVoDbpypfpKBR04IxHX1FFAz5NhsJwIEXsw4RHjFhHGet8mnq1uQObs5Z9RRcKw";
        String[] chunks = jwt.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String json = new String(decoder.decode(chunks[1]));

        try {
            JSONObject jsonObject = new JSONObject(json);
            String result = jsonObject.getString("sub");
            System.out.println(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testObjectLink(){
        UserEntity userEntity = new UserEntity();
        userEntity.setTelephoneNumber("first@mail.com");
        changeEmail(userEntity);
        System.out.println(userEntity.getTelephoneNumber());
    }

    void changeEmail(UserEntity userEntity){
        userEntity.setTelephoneNumber("second@mail.com");
    }


    @Autowired
    ServletContext context;

    @Test
    void testUploadPath(){


        String absolutePath = context.getRealPath("resources/static/images");
        absolutePath = absolutePath.replace("webapp\\","");
        System.out.println(new File(absolutePath + "\\GGG.txt").delete());
    }

}
