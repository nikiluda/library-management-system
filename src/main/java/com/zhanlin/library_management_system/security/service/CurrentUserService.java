package com.zhanlin.library_management_system.security.service;

import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.exceptions.UserReaderNotFoundException;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import com.zhanlin.library_management_system.models.LibraryUser;
import com.zhanlin.library_management_system.models.Role;
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

        LibraryUser user =  userRepository.findById(userId)
                .orElseThrow(() -> new UserReaderNotFoundException(
                        ErrorCode.USER_READER_NOT_FOUND,
                        ApiErrorMessage.USER_READER_NOT_FOUND.getMessage()
                ));

        if (user.getReader() == null) {
            throw new UserReaderNotFoundException(
                    ErrorCode.USER_READER_NOT_FOUND,
                    ApiErrorMessage.USER_READER_NOT_FOUND.getMessage()
            );
        }

        return user.getReader().getId();

    }

    public Role getCurrentUserRole() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(grantedAuthority ->
                        Role.valueOf(
                                grantedAuthority.getAuthority()
                                        .replace("ROLE_", "")
                        )
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Current user has no role"
                ));

    }
}
