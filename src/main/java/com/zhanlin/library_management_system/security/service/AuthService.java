package com.zhanlin.library_management_system.security.service;

import com.zhanlin.library_management_system.models.LibraryUser;
import com.zhanlin.library_management_system.models.Role;
import com.zhanlin.library_management_system.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public String login(String email, String password) {

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        return jwtService.generateToken(authenticated);
    }

    //TODO: сделать кастомное исключение
    public String register(String email, String password) {

        if (userRepository.existsByEmail(email))
            throw new IllegalArgumentException("Email already exists");

        String passwordHash = passwordEncoder.encode(password);

        LibraryUser user = new LibraryUser(
                email,
                passwordHash,
                Role.USER
        );

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                email,
                password
        );

        Authentication authenticated = authenticationManager.authenticate(authentication);

        return jwtService.generateToken(authenticated);
    }

}
