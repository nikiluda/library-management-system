package com.zhanlin.library_management_system.dto.bookLoan;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BookLoanRequestDto(

        @NotNull @Positive
        Long bookId,

        @NotNull @Positive
        Long readerId,

        @FutureOrPresent
        LocalDate dueDate
) {
}
