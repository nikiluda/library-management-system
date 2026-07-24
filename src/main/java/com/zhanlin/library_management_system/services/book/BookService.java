
package com.zhanlin.library_management_system.services.book;


import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;

import java.util.List;
import java.util.Optional;


public interface BookService {
    BookResponseDto createBook(BookRequestDto dto);

    BookResponseDto getBookById(Long id);

    BookResponseDto updateBook(Long id, BookRequestDto dto);

    void deleteBook(Long id);

    List<BookResponseDto> searchByTitle(String title);

    List<BookResponseDto> searchByAuthor(String author);

    List<BookResponseDto> search(String title, String author);

    Optional<BookResponseDto> getBookByIsbn(String isbn);

    List<BookResponseDto> getAllBooks();
}
