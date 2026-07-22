package com.zhanlin.library_management_system.services.bookLoan;

import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;

import java.util.List;

public interface BookLoanService {

    BookLoanResponseDto loanBook(BookLoanRequestDto dto);

    BookLoanResponseDto returnBook(Long loanId);

    List<BookLoanResponseDto> getLoansByReader(Long readerId);

    List<BookLoanResponseDto> getOverdueLoans();

    List<BookLoanResponseDto> getAllLoans();
}

