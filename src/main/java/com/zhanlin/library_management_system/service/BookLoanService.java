package com.zhanlin.library_management_system.service;

import com.zhanlin.library_management_system.dto.BookFilterDto;
import com.zhanlin.library_management_system.dto.LoanFilterDto;
import com.zhanlin.library_management_system.dto.PageResponse;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookLoanService {

    BookLoanResponseDto loanBook(BookLoanRequestDto dto);

    BookLoanResponseDto returnBook(Long loanId);

    List<BookLoanResponseDto> getLoansByReader(Long readerId);

    List<BookLoanResponseDto> getOverdueLoans();

    PageResponse<BookLoanResponseDto> getAllLoans(LoanFilterDto filterDto, Pageable pageable);


}

