//package com.example.demo.ReqResContextSettings;
//
//import org.jetbrains.annotations.NotNull;
//import org.json.JSONException;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebExceptionHandler;
//import reactor.core.publisher.Mono;
//
//@Component
//public class ExceptionsHandler implements WebExceptionHandler {
//
//    @NotNull
//    @Override
//    public Mono<Void> handle(@NotNull ServerWebExchange serverWebExchange, @NotNull Throwable throwable) {
//        if(throwable instanceof JSONException){
//            return ServerResponse.status(HttpStatus.BAD_REQUEST).body("JSONException", String.class).then();
//        }
//        else return ServerResponse.status(HttpStatus.BAD_REQUEST).body("Exception", String.class).then();
//    }
//
//}
