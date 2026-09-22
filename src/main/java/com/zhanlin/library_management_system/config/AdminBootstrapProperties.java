package com.zhanlin.library_management_system.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "app.admin")
@Validated
public record AdminBootstrapProperties(
        @NotBlank String email,
        @NotBlank String password
) {
}