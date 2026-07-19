
package com.zhanlin.library_management_system.services.book;


import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;

public interface BookService {
    BookResponseDto createBook(BookRequestDto dto);

    BookResponseDto getBookById(Long id);

    BookResponseDto updateBook(Long id, BookRequestDto dto);

    void deleteBook(Long id);
}
