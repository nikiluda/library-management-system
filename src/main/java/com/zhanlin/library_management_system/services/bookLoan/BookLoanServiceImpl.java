package com.zhanlin.library_management_system.services.bookLoan;

import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import com.zhanlin.library_management_system.exceptions.*;
import com.zhanlin.library_management_system.mappers.BookLoanMapper;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.models.BookLoan;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.BookLoanRepository;
import com.zhanlin.library_management_system.repository.BookRepository;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookLoanServiceImpl implements BookLoanService{

    private final BookLoanRepository bookLoanRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final BookLoanMapper bookLoanMapper;

    public BookLoanServiceImpl(BookLoanRepository bookLoanRepository, BookRepository bookRepository, ReaderRepository readerRepository, BookLoanMapper bookLoanMapper) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.bookLoanMapper = bookLoanMapper;
    }


    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book with ID " + id + " not found"));
    }

    private Reader findReader(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException(
                        "Reader with ID " + id + " not found"));
    }

    private BookLoan findLoan(Long id) {
        return bookLoanRepository.findById(id)
                .orElseThrow(() -> new BookLoanNotFoundException(
                        "Loan with ID " + id + " not found"));
    }

    @Override
    public BookLoanResponseDto loanBook(BookLoanRequestDto dto) {
        Book book = findBook(dto.bookId());
        Reader reader = findReader(dto.readerId());
        if (book.getAvailableCopies() <= 0) {
            throw new NoAvailableCopiesException("No available copies for book: " + book.getTitle());
        }

        book.setAvailableCopies(
                book.getAvailableCopies() - 1
        );


        BookLoan bookLoan = new BookLoan();
        bookLoan.setBook(book);
        bookLoan.setReader(reader);
        bookLoan.setLoanDate(LocalDate.now());
        bookLoan.setDueDate(dto.dueDate());
        bookLoan.setStatus(BookLoan.LoanStatus.ACTIVE);

        BookLoan savedLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDto(savedLoan);
    }

    @Override
    public BookLoanResponseDto returnBook(Long loanId) {
        BookLoan bookLoan = findLoan(loanId);

        if (bookLoan.getStatus() == BookLoan.LoanStatus.RETURNED)
            throw new BookAlreadyReturnedException("This book has already been returned");


        bookLoan.setReturnDate(LocalDate.now());
        bookLoan.setStatus(BookLoan.LoanStatus.RETURNED);
        Book book = bookLoan.getBook();

        book.setAvailableCopies(
                book.getAvailableCopies() + 1
        );
        BookLoan savedLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDto(savedLoan);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookLoanResponseDto> getLoansByReader(Long readerId) {
        findReader(readerId);
        List<BookLoan> loans = bookLoanRepository.findByReaderId(readerId);

        return loans.stream().map(bookLoanMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookLoanResponseDto> getOverdueLoans() {
        List<BookLoan> loans = bookLoanRepository.findByStatusAndDueDateBefore(BookLoan.LoanStatus.ACTIVE, LocalDate.now());

        return loans.stream().map(bookLoanMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookLoanResponseDto> getAllLoans() {

        return bookLoanRepository.findAll().stream().map(bookLoanMapper::toDto).toList();
    }


}
