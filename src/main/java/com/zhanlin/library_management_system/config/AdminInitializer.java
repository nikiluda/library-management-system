package com.zhanlin.library_management_system.config;

import com.zhanlin.library_management_system.models.LibraryUser;
import com.zhanlin.library_management_system.models.Role;
import com.zhanlin.library_management_system.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            if (!userRepository.existsByEmail("admin@test.com")) {

                LibraryUser admin = new LibraryUser(
                        "admin@test.com",
                        passwordEncoder.encode("12345678"),
                        Role.ADMIN
                );

                userRepository.save(admin);
            }
        };
    }
}