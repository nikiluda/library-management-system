package com.zhanlin.library_management_system.dto.reader;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReaderRequestDto(

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String firstName,

        @Size(max = 100)
        @NotBlank(message = "Last name is required")
        String lastName,

        @Size(max = 255)
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,

        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[1-9]\\d{10,14}$", message = "Phone must be valid (e.g. +79991234567)")
        String phone


) {
}
