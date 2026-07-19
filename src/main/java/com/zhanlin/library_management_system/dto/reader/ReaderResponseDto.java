package com.zhanlin.library_management_system.dto.reader;

import java.time.LocalDate;

public record ReaderResponseDto(

        Long id,
        String firstName,
        String lastName,
        String email,
        String phone,
        LocalDate registrationDate
) {
}
