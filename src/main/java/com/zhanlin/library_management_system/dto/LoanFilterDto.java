package com.zhanlin.library_management_system.dto;

import com.zhanlin.library_management_system.models.BookLoan;

import java.time.LocalDate;

public record LoanFilterDto(
        BookLoan.LoanStatus status,
        LocalDate loanDateFrom,
        LocalDate loanDateTo,
        LocalDate dueDateFrom,
        LocalDate dueDateTo,
        String bookTitle,
        String readerName
) {
}
