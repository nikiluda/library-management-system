package com.zhanlin.library_management_system.mappers;


import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.models.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public Book toEntity(BookRequestDto dto) {
        if (dto == null) return null;

        Book book = new Book();
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setIsbn(dto.isbn());
        book.setPublicationYear(dto.publicationYear());
        book.setTotalCopies(dto.totalCopies());
        book.setAvailableCopies(dto.totalCopies());
        return book;
    }

    public void updatedBook(BookRequestDto dto , Book book) {
        book.setTitle(dto.title());
        book.setAuthor(dto.author());
        book.setIsbn(dto.isbn());
        book.setPublicationYear(dto.publicationYear());
        book.setTotalCopies(dto.totalCopies());
    }


    public BookResponseDto toDto(Book book) {
        if (book == null) return null;

        return new BookResponseDto(book.getId(), book.getTitle(), book.getAuthor(),
                book.getIsbn(), book.getPublicationYear(), book.getTotalCopies(),
                book.getAvailableCopies()
        );
    }
}
