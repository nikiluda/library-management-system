package com.zhanlin.library_management_system.services.book;

import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.exceptions.BookNotFoundException;
import com.zhanlin.library_management_system.mappers.BookMapper;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private static final Logger log = LoggerFactory.getLogger(BookServiceImpl.class);


    public BookServiceImpl(BookRepository bookRepository, BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    private Book findBookById(Long id) {
        log.debug("Searching book by id={}", id);

        Book book =  bookRepository.findById(id)
                .orElseThrow(() -> new  BookNotFoundException(
                "Book with ID " + id + " not found"));

        log.debug("Book found: id={}, title={}", book.getId(), book.getTitle() );

        return book;
    }

    @Override
    public BookResponseDto createBook(BookRequestDto dto) {
        log.debug("Creating book with ISBN={}", dto.isbn());

        Book book = bookMapper.toEntity(dto);
        Book savedBook = bookRepository.save(book);
        log.info("Book created: id={}, title={}, isbn={}",savedBook.getId(),savedBook.getTitle(), savedBook.getIsbn());
        return bookMapper.toDto(savedBook);
    }

    @Override
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        log.debug("Updating book with id={}",id);

        Book book = findBookById(id);

        bookMapper.updateBook(dto, book);

        Book updatedBook = bookRepository.save(book);
        log.info("Book updated: id={}, title={}", updatedBook.getId(), updatedBook.getTitle());
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
        log.info("Book deleted: id={}, title={}", book.getId(), book.getTitle());
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

        log.debug("Searching book: title={}, author={}", titleQuery, authorQuery);

        List<BookResponseDto> books =  bookRepository.findByTitleContainingIgnoreCaseAndAuthorContainingIgnoreCase(titleQuery, authorQuery)
                .stream()
                .map(bookMapper::toDto)
                .toList();

        log.debug("Found {} books", books.size());
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
        log.debug("Fetching all books");

        List<BookResponseDto> books =  bookRepository.findAll().stream().map(bookMapper::toDto).toList();
        log.debug("Fetched {} books", books.size());
        return books;
    }
}