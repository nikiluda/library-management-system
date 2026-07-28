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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(BookLoanServiceImpl.class);

    public BookLoanServiceImpl(BookLoanRepository bookLoanRepository, BookRepository bookRepository, ReaderRepository readerRepository, BookLoanMapper bookLoanMapper) {
        this.bookLoanRepository = bookLoanRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.bookLoanMapper = bookLoanMapper;
    }


    private Book findBook(Long id) {
        log.debug("Searching book by id={}", id);
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(
                        "Book with ID " + id + " not found"));
    }

    private Reader findReader(Long id) {
        log.debug("Searching reader by id={}", id);
        return readerRepository.findById(id)
                .orElseThrow(() -> new ReaderNotFoundException(
                        "Reader with ID " + id + " not found"));
    }

    private BookLoan findLoan(Long id) {
        log.debug("Searching loan by id={}", id);
        return bookLoanRepository.findById(id)
                .orElseThrow(() -> new BookLoanNotFoundException(
                        "Loan with ID " + id + " not found"));
    }

    @Override
    public BookLoanResponseDto loanBook(BookLoanRequestDto dto) {
        log.debug("Creating loan with bookId={}, readerId={}", dto.bookId(), dto.readerId());
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


        LocalDate targetDueDate = (dto.dueDate() != null)
                ? dto.dueDate()
                :LocalDate.now().plusDays(14);
        bookLoan.setDueDate(targetDueDate);
        bookLoan.setStatus(BookLoan.LoanStatus.ACTIVE);

        BookLoan savedLoan = bookLoanRepository.save(bookLoan);

        log.info(
                "Book issued: loanId={}, bookId={}, readerId={}, dueDate={}",
                savedLoan.getId(),
                book.getId(),
                reader.getId(),
                savedLoan.getDueDate()
        );

        return bookLoanMapper.toDto(savedLoan);
    }

    @Override
    public BookLoanResponseDto returnBook(Long loanId) {
        log.debug("Returning book for loanId={}", loanId);
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
        log.info(
                "Book returned: loanId={}, bookId={}, readerId={}",
                savedLoan.getId(),
                book.getId(),
                bookLoan.getReader().getId()
        );

        return bookLoanMapper.toDto(savedLoan);
    }


    @Override
    @Transactional(readOnly = true)
    public List<BookLoanResponseDto> getLoansByReader(Long readerId) {
        log.debug("Fetching loans for readerId={}", readerId);
        findReader(readerId);
        List<BookLoan> loans = bookLoanRepository.findByReaderId(readerId);
        log.info(
                "Found {} loans for readerId={}",
                loans.size(),
                readerId
        );

        return loans.stream().map(bookLoanMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookLoanResponseDto> getOverdueLoans() {
        log.debug("Fetching overdue loans");
        List<BookLoan> loans = bookLoanRepository.findByStatusAndDueDateBefore(BookLoan.LoanStatus.ACTIVE, LocalDate.now());

        log.info("Found {} overdue loans", loans.size());
        return loans.stream().map(bookLoanMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<BookLoanResponseDto> getAllLoans() {

        log.debug("Fetching all loans");
        List<BookLoanResponseDto> loans = bookLoanRepository.findAll().stream().map(bookLoanMapper::toDto).toList();
        log.info("Fetched {} loans", loans.size());
        return loans;
    }


}
