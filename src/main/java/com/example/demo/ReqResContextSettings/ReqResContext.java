//package com.example.demo.ReqResContextSettings;
//
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//
//public final class ReqResContext implements AutoCloseable {
//
//    private static ThreadLocal<ReqResContext> instance = new ThreadLocal<>();
//
//    private ServerHttpRequest request;
//    private ServerHttpResponse response;
//
//    private ReqResContext(ServerHttpRequest request, ServerHttpResponse response) {
//        this.request = request;
//        this.response = response;
//    }
//
//    public static ReqResContext create(ServerHttpRequest request, ServerHttpResponse response) {
//        ReqResContext context = new ReqResContext(request, response);
//        instance.set(context);
//        return context;
//    }
//
//    public static ReqResContext getCurrentInstance() {
//        return instance.get();
//    }
//
//    @Override
//    public void close() {
//        instance.remove();
//    }
//
//    public ServerHttpRequest getRequest() {
//        return request;
//    }
//
//    public ServerHttpResponse getResponse() {
//        return response;
//    }
//}
