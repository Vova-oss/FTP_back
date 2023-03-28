package com.example.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig  {

    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public WebSecurityConfig(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    public PasswordEncoder PasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity){
        return httpSecurity
                .cors(corsSpec -> corsSpec.configurationSource(corsConfiguration()))
                .exceptionHandling()
                .authenticationEntryPoint(
                        (swe, e) ->
                                Mono.fromRunnable(
                                        () -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
                                )
                )
                .accessDeniedHandler(
                        (swe, e) ->
                                Mono.fromRunnable(
                                        () -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
                                )
                )
                .and()

                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange()
                .pathMatchers(HttpMethod.DELETE, "/user/deleteByTelephoneNumber").hasAuthority("ADMIN")
                .pathMatchers("/user/checkRole").authenticated()
                .pathMatchers("/user/checkPassword/*").hasAuthority("USER")
                .pathMatchers("/user/changeGender/*").hasAuthority("USER")
                .pathMatchers("/user/changeFIO/*").hasAuthority("USER")
                .pathMatchers("/user/**").permitAll()

                .pathMatchers("/type/getAll").permitAll()
                .pathMatchers("/type/**").hasAuthority("ADMIN")

                .pathMatchers("/device/getByParams").permitAll()
                .pathMatchers("/device/getById/**").permitAll()
                .pathMatchers("/device/getTopDevices").permitAll()
                .pathMatchers("/device/**").hasAuthority("ADMIN")

                .pathMatchers("/brand/**").hasAuthority("ADMIN")

                .pathMatchers("/order/add").hasAuthority("USER")
                .pathMatchers("/order/getAllByUser").hasAuthority("USER")
                .pathMatchers("/order/changeStatusOfOrder").hasAuthority("ADMIN")
                .pathMatchers("/order/getAll").hasAuthority("ADMIN")

                .pathMatchers("/refreshToken").permitAll()

                .anyExchange().permitAll()

                .and()
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.applyPermitDefaultValues();
        corsConfig.setAllowCredentials(false);
        corsConfig.addAllowedMethod("POST");
        corsConfig.addAllowedMethod("GET");
        corsConfig.addAllowedMethod("PUT");
        corsConfig.addAllowedMethod("DELETE");
        corsConfig.addAllowedOriginPattern("*");
        corsConfig.addExposedHeader("*");
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        source.registerCorsConfiguration("/user/**", corsConfig);
        source.registerCorsConfiguration("/order/**", corsConfig);
        source.registerCorsConfiguration("/device/**", corsConfig);
        source.registerCorsConfiguration("/brand/**", corsConfig);
        source.registerCorsConfiguration("/type/**", corsConfig);
        return source;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        return new CorsWebFilter(corsConfiguration());
    }

}
