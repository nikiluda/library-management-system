package com.zhanlin.library_management_system.security.web;

import com.zhanlin.library_management_system.security.dto.LoginRequest;
import com.zhanlin.library_management_system.security.dto.AuthResponse;
import com.zhanlin.library_management_system.security.dto.RegisterRequest;
import com.zhanlin.library_management_system.security.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        String token = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok( new AuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        String token =  authService.register(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(new AuthResponse(token));
    }

}
