package com.zhanlin.library_management_system.dto.bookLoan;

import java.time.LocalDate;

public record BookLoanRequestDto(

        Long bookId,
        Long readerId,
        LocalDate dueDate
) {
}
