
package com.zhanlin.library_management_system.service;


import com.zhanlin.library_management_system.dto.BookFilterDto;
import com.zhanlin.library_management_system.dto.PageResponse;
import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;


public interface BookService {
    BookResponseDto createBook(BookRequestDto dto);

    BookResponseDto getBookById(Long id);

    BookResponseDto updateBook(Long id, BookRequestDto dto);

    void deleteBook(Long id);

    List<BookResponseDto> searchByTitle(String title);

    List<BookResponseDto> searchByAuthor(String author);

    PageResponse<BookResponseDto> search(BookFilterDto filter, Pageable pageable);

    Optional<BookResponseDto> getBookByIsbn(String isbn);

    PageResponse<BookResponseDto> getAllBooks(BookFilterDto filter, Pageable pageable);
}
