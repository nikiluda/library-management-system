package com.zhanlin.library_management_system.controllers;


import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.messages.ApiMessage;
import com.zhanlin.library_management_system.models.response.ApiResponse;
import com.zhanlin.library_management_system.service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //TODO : переделать под пагинацию
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> getBookById(@PathVariable Long id) {
        BookResponseDto bookResponseDto = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.createSuccessful(bookResponseDto));
    }


    @PostMapping
    public ResponseEntity<ApiResponse<BookResponseDto>> createBook( @Valid @RequestBody BookRequestDto dto) {

        BookResponseDto bookResponseDto = bookService.createBook(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.createSuccessful(
                        ApiMessage.BOOK_CREATED.getMessage(),
                        bookResponseDto
                ));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponseDto>> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequestDto dto) {

         return ResponseEntity.ok(
                 ApiResponse.createSuccessful(
                         ApiMessage.BOOK_UPDATED.getMessage(),
                         bookService.updateBook(id, dto)
                 ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    //TODO: переделать поиск
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> search(@RequestParam(required = false) String title,
                                                        @RequestParam(required = false) String author) {
        return ResponseEntity.ok(bookService.search(title, author));


    }
}
