package com.zhanlin.library_management_system.security.bootstrap;


import com.zhanlin.library_management_system.models.LibraryUser;
import com.zhanlin.library_management_system.models.Role;
import com.zhanlin.library_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@EnableConfigurationProperties(AdminBootstrapProperties.class)
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminBootstrapProperties properties;


    @Override
    public void run(String... args) {

        if (userRepository.existsByEmail(properties.email())) {
            return;
        }

        LibraryUser admin = new LibraryUser(
                properties.email(),
                passwordEncoder.encode(properties.password()),
                Role.ADMIN
        );

        userRepository.save(admin);
    }
}
