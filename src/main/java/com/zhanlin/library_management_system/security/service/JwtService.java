package com.zhanlin.library_management_system.security.service;

import com.zhanlin.library_management_system.security.LibraryUserDetails;
import com.zhanlin.library_management_system.security.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.getSecret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }


    public String generateToken(Authentication authentication) {

        LibraryUserDetails userDetails = (LibraryUserDetails) authentication.getPrincipal();

        Long userId = userDetails.getUser().getId();

        return Jwts.builder()
                .subject(userId.toString())
                .claim(
                        "roles",
                        userDetails.getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .toList()
                )
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis() + jwtProperties.getExpiration()
                        )
                )
                .signWith(secretKey)
                .compact();

    }

}
