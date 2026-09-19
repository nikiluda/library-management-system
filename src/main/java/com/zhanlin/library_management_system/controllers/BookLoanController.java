package com.zhanlin.library_management_system.controllers;


import com.zhanlin.library_management_system.dto.LoanFilterDto;
import com.zhanlin.library_management_system.dto.PageResponse;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import com.zhanlin.library_management_system.messages.ApiMessage;
import com.zhanlin.library_management_system.models.response.ApiResponse;
import com.zhanlin.library_management_system.service.BookLoanService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class BookLoanController {


    private final BookLoanService bookLoanService;

    public BookLoanController(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookLoanResponseDto>> loanBook(@Valid @RequestBody BookLoanRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessful(
                        ApiMessage.BOOK_LOAN_CREATED.getMessage(),
                        bookLoanService.loanBook(dto)
                ));

    }

    @PutMapping("/{id}/return")
    public ResponseEntity<ApiResponse<BookLoanResponseDto>> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.createSuccessful(
                        ApiMessage.BOOK_RETURNED.getMessage(),
                        bookLoanService.returnBook(id)
                )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookLoanResponseDto>>> getAllLoans(
            LoanFilterDto filterDto,
            @PageableDefault(
                    size = 20,
                    sort = {"id"},
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.createSuccessful(
                "Loans: ",
                bookLoanService.getAllLoans(filterDto, pageable)
        ));
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<ApiResponse<PageResponse<BookLoanResponseDto>>> getLoansByReader(
            @PathVariable Long readerId,
            @PageableDefault(
                    size = 20,
                    sort = {"loanDate"},
                    direction = Sort.Direction.ASC)
            Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.createSuccessful(bookLoanService.getLoansByReader(readerId, pageable)));
    }


    //TODO: изменить под пагинацию и ResponsePagination
    @GetMapping("/overdue")
    public ResponseEntity<List<BookLoanResponseDto>> getOverdueLoans() {
        return ResponseEntity.ok(bookLoanService.getOverdueLoans());
    }





}
