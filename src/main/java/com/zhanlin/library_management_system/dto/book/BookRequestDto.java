package com.zhanlin.library_management_system.dto.book;

import jakarta.validation.constraints.*;

public record BookRequestDto(

        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Author is required")
        String author,

        @NotNull(message = "Publication year is required")
        @Min(value = 1650, message = "Year must be between 1650 and 2026")
        @Max(value = 2026, message = "Year must be between 1650 and 2026")
        Integer publicationYear,

        @NotBlank(message = "ISBN is required")
        @Pattern(regexp = "^(\\d{10}|\\d{13})$", message = "ISBN must be exactly 10 or 13 digits")
        String isbn,

        @NotNull(message = "Total copies count is required")
        @Min(value = 0 , message = "Total copies cannot be negative")
        Integer totalCopies
) {
}
