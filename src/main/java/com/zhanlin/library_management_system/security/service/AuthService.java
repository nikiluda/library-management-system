package com.zhanlin.library_management_system.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    public String login(String email, String password) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        return jwtService.generateToken(authenticated);
    }

}
