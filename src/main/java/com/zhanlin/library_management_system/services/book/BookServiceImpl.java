package com.zhanlin.library_management_system.services.book;

import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.mappers.BookMapper;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;

    }

    //добавить исключение  - > BookNotFoundException
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: " + id + " not found"));
    }

    @Override
    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Transactional
    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = findBookById(id);

        bookMapper.updatedBook(dto, book);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);

    }

    @Transactional(readOnly = true)
    public BookResponseDto getBookById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toDto(book);
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookById(id);
        bookRepository.delete(book);
    }


}