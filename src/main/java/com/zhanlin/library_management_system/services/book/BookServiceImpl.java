package com.zhanlin.library_management_system.services.book;

import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.mappers.BookMapper;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.repository.BookRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper, BookService bookService) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    //добавить исключение  - > BookNotFoundException
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: " + id + " not found"));
    }

    @Override
    public BookResponseDto createBook(BookRequestDto dto) {
        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = findBookById(id);

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

    @Transactional(readOnly = true)
    @Override
    public Optional<BookResponseDto> getBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn).map(bookMapper::toDto);
    }


}