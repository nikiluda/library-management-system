package com.zhanlin.library_management_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("dev")
@Order(1)
public class DevSecurityConfig {

    @Bean
    public SecurityFilterChain h2SecurityFilterChain(HttpSecurity http) throws Exception {

        http
                .securityMatcher("/h2-console/**")
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers.frameOptions(frame -> frame.sameOrigin())
                )
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll()
                );

        return http.build();
    }
}