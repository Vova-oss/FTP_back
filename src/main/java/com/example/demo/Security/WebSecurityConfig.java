package com.example.demo.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

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

}
