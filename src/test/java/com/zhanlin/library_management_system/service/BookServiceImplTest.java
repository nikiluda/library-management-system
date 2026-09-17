package com.zhanlin.library_management_system.service;


import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.exceptions.ResourceAlreadyExistsException;
import com.zhanlin.library_management_system.exceptions.ResourceNotFoundException;
import com.zhanlin.library_management_system.mappers.BookMapper;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.repository.BookRepository;
import com.zhanlin.library_management_system.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willAnswer;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void createBook_shouldThrowException_whenIsbnAlreadyExists() {

        when(bookRepository.existsByIsbn("5050007657"))
                .thenReturn(true);

        BookRequestDto dto = new BookRequestDto(
                "Война и мир",
                "Толстой",
                1863,
                "5050007657",
                1
        );

        assertThrows(ResourceAlreadyExistsException.class,
                () -> bookService.createBook(dto));
    }

    @Test
    void createBook_shouldCreateBook_whenIsbnAvailable() {


        BookRequestDto dto = new BookRequestDto(
                "Война и мир",
                "Толстой",
                1863,
                "5050007657",
                1
        );

        given(bookRepository.existsByIsbn(dto.isbn()))
                .willReturn(false);

        Book bookEntity = new Book();
        given(bookMapper.toEntity(dto))
                .willReturn(bookEntity);

        Book savedBook = new Book();
        savedBook.setId(1L);
        given(bookRepository.save(bookEntity))
                .willReturn(savedBook);

        BookResponseDto expectedResponse = new BookResponseDto(
                1L,
                "Война и мир",
                "Толстой",
                "5050007657",
                1863,
                1,
                1
        );
        given(bookMapper.toDto(savedBook))
                .willReturn(expectedResponse);




        BookResponseDto result = bookService.createBook(dto);


        assertThat(result).isEqualTo(expectedResponse);
        verify(bookRepository).save(bookEntity);
    }

    @Test
    void getBookById_shouldReturnBook_whenBookExists() {

        Long id = 1L;
        Book book = new Book();
        BookResponseDto expectedResponse = new BookResponseDto(
                id,
                "Война и мир",
                "Толстой",
                "5050007657",
                1863,
                1,
                1
        );


        given(bookRepository.findById(id)).willReturn(Optional.of(book));
        given(bookMapper.toDto(book)).willReturn(expectedResponse);

        BookResponseDto result = bookService.getBookById(id);

        assertThat(result).isEqualTo(expectedResponse);
        verify(bookRepository).findById(id);
    }

    @Test
    void getBookById_shouldThrowException_whenBookNotFound() {

        Long id = 1L;
        given(bookRepository.findById(id)).willReturn(Optional.empty());


        assertThatThrownBy(() -> bookService.getBookById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting(ex -> ((ResourceNotFoundException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BOOK_NOT_FOUND);

        verify(bookMapper, never()).toDto(any());
    }

    @Test
    void updateBook_shouldUpdateBook_whenBookExists() {

        Long id = 1L;
        BookRequestDto dto = new BookRequestDto("Война и мир", "Толстой", 1863, "5050007657", 1);

        Book book = new Book();
        book.setIsbn("5050007657");

        Book updatedBook = new Book();
        updatedBook.setIsbn(dto.isbn());

        BookResponseDto expectedResponse = new BookResponseDto(
                id, "Война и мир", "Толстой", "5050007657", 1863, 1, 1
        );

        given(bookRepository.findById(id)).willReturn(Optional.of(book));

        willAnswer(invocationOnMock -> {
            Book target = invocationOnMock.getArgument(1);
            target.setTitle(dto.title());
            target.setAuthor(dto.author());
            target.setIsbn(dto.isbn());
            target.setPublicationYear(dto.publicationYear());
            target.setTotalCopies(dto.totalCopies());
            return null;
        }).given(bookMapper).updateBook(dto, book);

        given(bookRepository.save(book)).willReturn(updatedBook);
        given(bookMapper.toDto(updatedBook)).willReturn(expectedResponse);

        BookResponseDto result = bookService.updateBook(id, dto);

        assertThat(result).isEqualTo(expectedResponse);
        verify(bookRepository).save(book);

    }

    @Test
    void updateBook_shouldThrowException_whenBookNotFound() {

        Long id = 1L;
        BookRequestDto dto = new BookRequestDto(
                "Война и мир",
                "Толстой",
                1863,
                "5050007657",
                1
        );
        given(bookRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.updateBook(id, dto))
                .isInstanceOf(ResourceNotFoundException.class)
                .extracting(ex -> ((ResourceNotFoundException) ex).getErrorCode())
                .isEqualTo(ErrorCode.BOOK_NOT_FOUND);

        verify(bookRepository, never()).save(any());
        verify(bookMapper, never()).toDto(any());

    }

}
