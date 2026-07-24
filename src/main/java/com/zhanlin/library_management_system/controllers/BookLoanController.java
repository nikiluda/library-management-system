package com.zhanlin.library_management_system.controllers;


import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import com.zhanlin.library_management_system.services.bookLoan.BookLoanService;
import com.zhanlin.library_management_system.services.reader.ReaderService;
import jakarta.validation.Valid;
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
    public ResponseEntity<BookLoanResponseDto> loanBook(@RequestBody @Valid BookLoanRequestDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookLoanService.loanBook(dto));

    }

    @PutMapping("/{id}/return")
    public ResponseEntity<BookLoanResponseDto> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookLoanService.returnBook(id));
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<BookLoanResponseDto>> getLoansByReader(@PathVariable Long readerId) {
        return ResponseEntity.ok(bookLoanService.getLoansByReader(readerId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BookLoanResponseDto>> getOverdueLoans() {
        return ResponseEntity.ok(bookLoanService.getOverdueLoans());
    }

    @GetMapping
    public ResponseEntity<List<BookLoanResponseDto>> getAllLoans() {
        return ResponseEntity.ok(bookLoanService.getAllLoans());
    }


}
