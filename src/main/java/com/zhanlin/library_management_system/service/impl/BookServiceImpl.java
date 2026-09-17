package com.zhanlin.library_management_system.service.impl;

import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.exceptions.ResourceAlreadyExistsException;
import com.zhanlin.library_management_system.exceptions.ResourceNotFoundException;
import com.zhanlin.library_management_system.logging.annotation.Audit;
import com.zhanlin.library_management_system.logging.annotation.AuditAction;
import com.zhanlin.library_management_system.mappers.BookMapper;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.repository.BookRepository;

import com.zhanlin.library_management_system.service.BookService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    private Book findBookById(Long id) {

        Book book =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.BOOK_NOT_FOUND,
                        ApiErrorMessage.BOOK_NOT_FOUND_BY_ID.getMessage(id)
                ));
        return book;
    }

    @Override
    @Audit(AuditAction.BOOK_CREATED)
    public BookResponseDto createBook(BookRequestDto dto) {

        if (bookRepository.existsByIsbn(dto.isbn())) {
            throw new ResourceAlreadyExistsException(
                    ErrorCode.ISBN_ALREADY_EXISTS,
                    ApiErrorMessage.ISBN_ALREADY_EXISTS.getMessage(dto.isbn())
            );
        }

        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    @Audit(AuditAction.BOOK_UPDATED)
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {

        Book book = findBookById(id);

        if (!book.getIsbn().equals(dto.isbn()) && bookRepository.existsByIsbn(dto.isbn())) {
            throw new ResourceAlreadyExistsException(
                    ErrorCode.ISBN_ALREADY_EXISTS,
                    ApiErrorMessage.ISBN_ALREADY_EXISTS.getMessage(dto.isbn())
            );
        }

        bookMapper.updateBook(dto, book);

        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);

    }

    @Transactional(readOnly = true)
    @Override
    public BookResponseDto getBookById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toDto(book);
    }


    @Override
    @Audit(AuditAction.BOOK_DELETED)
    public void deleteBook(Long id) {
        Book book = findBookById(id);

        bookRepository.delete(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookResponseDto> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title)
                .stream().map(bookMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookResponseDto> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author)
                .stream().map(bookMapper::toDto).toList();
    }


    //залогировать
    @Override
    public List<BookResponseDto> search(String title, String author) {
        String titleQuery = (title!=null) ? title : "";
        String authorQuery = (author!=null) ? author : "";


        List<BookResponseDto> books =  bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(titleQuery, authorQuery)
                .stream()
                .map(bookMapper::toDto)
                .toList();

        return books;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<BookResponseDto> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(bookMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookResponseDto> getAllBooks() {

        List<BookResponseDto> books =  bookRepository.findAll().stream().map(bookMapper::toDto).toList();
        return books;
    }
}