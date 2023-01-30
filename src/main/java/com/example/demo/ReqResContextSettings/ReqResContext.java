package com.example.demo.ReqResContextSettings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class ReqResContext implements AutoCloseable {

    private static ThreadLocal<ReqResContext> instance = new ThreadLocal<>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    private ReqResContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public static ReqResContext create(HttpServletRequest request, HttpServletResponse response) {
        ReqResContext context = new ReqResContext(request, response);
        instance.set(context);
        return context;
    }

    public static ReqResContext getCurrentInstance() {
        return instance.get();
    }

    @Override
    public void close() {
        instance.remove();
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }
}
