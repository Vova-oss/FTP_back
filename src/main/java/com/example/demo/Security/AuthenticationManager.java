package com.example.demo.Security;

import com.example.demo.Security.Service.JWTokenService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JWTokenService jwTokenService;

    public AuthenticationManager(JWTokenService jwTokenService) {
        this.jwTokenService = jwTokenService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();

        String username;
        try {
             username = jwTokenService.getNameFromJWT(authToken);
        }catch (Exception e){
            username = null;
            System.out.println(e);
        }

        if(username != null){
            String role = jwTokenService.getRoleFromJWT(authToken);

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    authorities
            );
            return Mono.just(usernamePasswordAuthenticationToken);
        }else {
            return Mono.empty();
        }

    }

}
