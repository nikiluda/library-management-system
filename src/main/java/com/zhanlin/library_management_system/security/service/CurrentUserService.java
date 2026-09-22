package com.zhanlin.library_management_system.security.service;

import com.zhanlin.library_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {

    private final UserRepository userRepository;

    public Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        Jwt jwt = (Jwt) authentication.getPrincipal();

        return Long.valueOf(jwt.getSubject());
    }

    public Long getCurrentReaderId() {

        Long userId = getCurrentUserId();

        return userRepository.findById(userId)
                .map(user -> user.getReader().getId())
                .orElseThrow();

    }
}
