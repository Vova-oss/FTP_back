//package com.example.demo.Configuration;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.web.reactive.config.CorsRegistry;
//import org.springframework.web.reactive.config.EnableWebFlux;
//import org.springframework.web.reactive.config.WebFluxConfigurer;
//import springfox.documentation.swagger.web.SecurityConfiguration;
//
//@Configuration
//@EnableWebFlux
//@Import({SecurityConfiguration.class})
//public class CorsConfig implements WebFluxConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .exposedHeaders("Access-Control-Allow-Origin",
//                        "Access-Control-Allow-Methods",
//                        "Access-Control-Allow-Headers",
//                        "Access-Control-Max-Age",
//                        "Access-Control-Request-Headers",
//                        "Access-Control-Request-Method")
//                .maxAge(3600);
//
//        registry.addMapping("/order/**")
//                .allowedOrigins("*")
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .exposedHeaders("Access-Control-Allow-Origin",
//                        "Access-Control-Allow-Methods",
//                        "Access-Control-Allow-Headers",
//                        "Access-Control-Max-Age",
//                        "Access-Control-Request-Headers",
//                        "Access-Control-Request-Method")
//                .maxAge(3600);
//    }
//}