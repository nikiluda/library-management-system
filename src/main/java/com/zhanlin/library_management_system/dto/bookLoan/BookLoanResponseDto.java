package com.zhanlin.library_management_system.dto.bookLoan;

import com.zhanlin.library_management_system.models.BookLoan;

import java.time.LocalDate;

public record BookLoanResponseDto(
        Long id,

        Long bookId,
        String bookTitle,

        Long readerId,
        String readerFullName,

        LocalDate loanDate,
        LocalDate dueDate,
        LocalDate returnDate,

        BookLoan.LoanStatus status
) {
}
