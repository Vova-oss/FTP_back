package com.example.demo.Controller.AuxiliaryClasses;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

public class CustomException extends Exception  {

    public CustomException(String message) {
        super(message);
    }
}
