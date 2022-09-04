package com.example.demo.Controller.AuxiliaryClasses;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.Entity.Response.ResponseClass;
import org.json.JSONException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin("http://localhost:3000")
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler(value = JSONException.class)
    public ResponseEntity<ResponseClass> ExceptionOfTokenExpired(HttpServletRequest request){
        System.out.println("Incorrect JSON");
        return ResponseEntity.status(400).body(new ResponseClass(400, "Incorrect JSON (EX)", request.getServletPath()));
    }

}
