package com.zhanlin.library_management_system.service;

import com.zhanlin.library_management_system.dto.LoanFilterDto;
import com.zhanlin.library_management_system.dto.PageResponse;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import org.springframework.data.domain.Pageable;



public interface BookLoanService {

    BookLoanResponseDto loanBook(BookLoanRequestDto dto);

    BookLoanResponseDto returnBook(Long loanId);

    PageResponse<BookLoanResponseDto> getLoansByReader(Long readerId, Pageable pageable);

    PageResponse<BookLoanResponseDto> getOverdueLoans(Pageable pageable);

    PageResponse<BookLoanResponseDto> getAllLoans(LoanFilterDto filterDto, Pageable pageable);

    PageResponse<BookLoanResponseDto> getLoansByCurrentUser(Pageable pageable);


}

