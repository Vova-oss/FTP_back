//package com.example.demo.ReqResContextSettings;
//
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//// Задаём самый высокий приоритет (он отрицательный, ибо нумерация фильтров идёт по порядку.
//// Чем меньше значение, тем раньше отрабатывает), чтобы данный фильтр отработал быстрее,
//// чем наше основное приложение
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class RequestFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        try (ReqResContext context = ReqResContext.create(request, response)) {
//            filterChain.doFilter(request, response);
//        }
//    }
//
//}
