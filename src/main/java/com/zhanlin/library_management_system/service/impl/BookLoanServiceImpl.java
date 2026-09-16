package com.zhanlin.library_management_system.service.impl;

import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import com.zhanlin.library_management_system.exceptions.*;
import com.zhanlin.library_management_system.logging.annotation.Audit;
import com.zhanlin.library_management_system.logging.annotation.AuditAction;
import com.zhanlin.library_management_system.mappers.BookLoanMapper;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.models.BookLoan;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.BookLoanRepository;
import com.zhanlin.library_management_system.repository.BookRepository;
import com.zhanlin.library_management_system.repository.ReaderRepository;

import com.zhanlin.library_management_system.service.BookLoanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookLoanServiceImpl implements BookLoanService {

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
        Book book =  bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.BOOK_NOT_FOUND,
                        ApiErrorMessage.BOOK_NOT_FOUND_BY_ID.getMessage(id)
                ));
        return book;
    }

    private Reader findReader(Long id) {
        Reader reader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.READER_NOT_FOUND,
                        ApiErrorMessage.READER_NOT_FOUND_BY_ID.getMessage(id)
                ));

        return reader;
    }

    private BookLoan findLoan(Long id) {

        BookLoan bookLoan = bookLoanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.LOAN_NOT_FOUND,
                        ApiErrorMessage.LOAN_NOT_FOUND_BY_ID.getMessage(id))
                );

        return bookLoan;

    }

    @Override
    @Audit(AuditAction.BOOK_ISSUED)
    public BookLoanResponseDto loanBook(BookLoanRequestDto dto) {

        Book book = findBook(dto.bookId());
        Reader reader = findReader(dto.readerId());

        if (book.getAvailableCopies() <= 0) {
            throw new BusinessRuleException(
                    ErrorCode.NO_AVAILABLE_COPIES,
                    ApiErrorMessage.NO_AVAILABLE_COPIES.getMessage(book.getTitle())
            );
        }

        boolean alreadyLoaned = bookLoanRepository
                .existsByBookIdAndReaderIdAndStatus(
                        dto.bookId(),
                        dto.readerId(),
                        BookLoan.LoanStatus.ACTIVE
                );

        if (alreadyLoaned) {
            throw new BusinessRuleException(
                    ErrorCode.ACTIVE_LOAN_EXISTS,
                    ApiErrorMessage.ACTIVE_LOAN_EXISTS.getMessage()
            );
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


        return bookLoanMapper.toDto(savedLoan);
    }

    @Override
    @Audit(AuditAction.BOOK_RETURNED)
    public BookLoanResponseDto returnBook(Long loanId) {
        BookLoan bookLoan = findLoan(loanId);


        if (bookLoan.getStatus() == BookLoan.LoanStatus.RETURNED)
            throw new BusinessRuleException(
                    ErrorCode.LOAN_ALREADY_RETURNED,
                    ApiErrorMessage.LOAN_ALREADY_RETURNED.getMessage(loanId)
            );


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

        List<BookLoanResponseDto> loans = bookLoanRepository.findAll().stream().map(bookLoanMapper::toDto).toList();
        return loans;
    }


}
