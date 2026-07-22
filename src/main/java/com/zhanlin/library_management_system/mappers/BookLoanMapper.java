package com.zhanlin.library_management_system.mappers;

import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import com.zhanlin.library_management_system.models.BookLoan;
import org.springframework.stereotype.Component;

@Component
public class BookLoanMapper {
    public BookLoanResponseDto toDto(BookLoan loan) {
        return new BookLoanResponseDto(
                loan.getId(),
                loan.getBook().getId(),
                loan.getBook().getTitle(),
                loan.getReader().getId(),
                loan.getReader().getFirstName() + " " + loan.getReader().getLastName(),
                loan.getLoanDate(),
                loan.getDueDate(),
                loan.getReturnDate(),
                loan.getStatus()
        );
    }

}
