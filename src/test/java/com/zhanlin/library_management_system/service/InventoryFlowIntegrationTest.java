package com.zhanlin.library_management_system.service;

import com.zhanlin.library_management_system.dto.book.BookRequestDto;
import com.zhanlin.library_management_system.dto.book.BookResponseDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanRequestDto;
import com.zhanlin.library_management_system.dto.bookLoan.BookLoanResponseDto;
import com.zhanlin.library_management_system.exceptions.BusinessRuleException;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.models.BookLoan;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.models.Role;
import com.zhanlin.library_management_system.repository.BookLoanRepository;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import com.zhanlin.library_management_system.security.service.CurrentUserService;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Date;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest(properties =
        "spring.datasource.url=jdbc:h2:mem:inventorytest;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE;DB_CLOSE_DELAY=-1")
class InventoryFlowIntegrationTest {

    private static final AtomicInteger IDS = new AtomicInteger();

    @Autowired
    private BookService bookService;

    @Autowired
    private BookLoanService loanService;

    @Autowired
    private BookLoanRepository loanRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Validator validator;

    @MockitoBean
    private CurrentUserService currentUserService;

    @Test
    void inventoryRemainsConsistentAcrossIssueResizeAndReturn() {
        int id = IDS.incrementAndGet();
        String isbn = String.format("978%010d", id);
        BookResponseDto book = bookService.createBook(
                new BookRequestDto("Inventory test", "Author", 2020, isbn, 2));
        Reader reader = createReader(id);
        LocalDate dueDate = LocalDate.now().plusDays(7);

        BookLoanResponseDto loan = loanService.loanBook(
                new BookLoanRequestDto(book.id(), reader.getId(), dueDate));

        assertThat(loan.dueDate()).isEqualTo(dueDate);
        assertThat(bookService.getBookById(book.id()).availableCopies()).isEqualTo(1);
        assertThat(loanRepository.countByBookIdAndStatus(book.id(), BookLoan.LoanStatus.ACTIVE))
                .isEqualTo(1);

        BookResponseDto enlarged = bookService.updateBook(book.id(),
                new BookRequestDto("Inventory test", "Author", 2020, isbn, 3));
        assertThat(enlarged.availableCopies()).isEqualTo(2);

        when(currentUserService.getCurrentUserRole()).thenReturn(Role.ADMIN);
        loanService.returnBook(loan.id());
        assertThat(bookService.getBookById(book.id()).availableCopies()).isEqualTo(3);

        BookResponseDto reduced = bookService.updateBook(book.id(),
                new BookRequestDto("Inventory test", "Author", 2020, isbn, 1));
        assertThat(reduced.totalCopies()).isEqualTo(1);
        assertThat(reduced.availableCopies()).isEqualTo(1);
        assertThat(loanRepository.countByBookIdAndStatus(book.id(), BookLoan.LoanStatus.ACTIVE))
                .isZero();
    }

    @Test
    void cannotRemoveIssuedCopyOrIssueMoreThanTotal() {
        int id = IDS.incrementAndGet();
        String isbn = String.format("978%010d", id);
        BookResponseDto book = bookService.createBook(
                new BookRequestDto("Single copy", "Author", 2020, isbn, 1));
        Reader firstReader = createReader(id);
        Reader secondReader = createReader(IDS.incrementAndGet());
        loanService.loanBook(new BookLoanRequestDto(
                book.id(), firstReader.getId(), LocalDate.now().plusDays(7)));

        assertThatThrownBy(() -> bookService.updateBook(book.id(),
                new BookRequestDto("Single copy", "Author", 2020, isbn, 0)))
                .isInstanceOfSatisfying(BusinessRuleException.class,
                        ex -> assertThat(ex.getErrorCode())
                                .isEqualTo(ErrorCode.TOTAL_COPIES_BELOW_ACTIVE_LOANS));
        assertThatThrownBy(() -> loanService.loanBook(new BookLoanRequestDto(
                book.id(), secondReader.getId(), LocalDate.now().plusDays(7))))
                .isInstanceOfSatisfying(BusinessRuleException.class,
                        ex -> assertThat(ex.getErrorCode())
                                .isEqualTo(ErrorCode.NO_AVAILABLE_COPIES));

        BookResponseDto unchanged = bookService.getBookById(book.id());
        assertThat(unchanged.totalCopies()).isEqualTo(1);
        assertThat(unchanged.availableCopies()).isZero();
        assertThat(loanRepository.countByBookIdAndStatus(book.id(), BookLoan.LoanStatus.ACTIVE))
                .isEqualTo(1);
    }

    @Test
    void dueDateMustBeProvided() {
        assertThat(validator.validate(new BookLoanRequestDto(1L, 1L, null)))
                .anySatisfy(violation -> assertThat(violation.getPropertyPath().toString())
                        .isEqualTo("dueDate"));
    }

    @Test
    void concurrentIssuesCannotCreateMoreLoansThanCopies() throws Exception {
        int id = IDS.incrementAndGet();
        BookResponseDto book = bookService.createBook(new BookRequestDto(
                "Concurrent issue", "Author", 2020, String.format("978%010d", id), 1));
        Reader firstReader = createReader(id);
        Reader secondReader = createReader(IDS.incrementAndGet());
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);

        try {
            Future<RuntimeException> first = executor.submit(
                    () -> issueAfterStart(start, book.id(), firstReader.getId()));
            Future<RuntimeException> second = executor.submit(
                    () -> issueAfterStart(start, book.id(), secondReader.getId()));
            start.countDown();

            RuntimeException firstFailure = first.get(10, TimeUnit.SECONDS);
            RuntimeException secondFailure = second.get(10, TimeUnit.SECONDS);
            int successfulIssues = (firstFailure == null ? 1 : 0)
                    + (secondFailure == null ? 1 : 0);
            assertThat(successfulIssues).isEqualTo(1);
            RuntimeException rejected = firstFailure != null ? firstFailure : secondFailure;
            assertThat(rejected).isInstanceOfAny(
                    BusinessRuleException.class, OptimisticLockingFailureException.class);
            assertThat(loanRepository.countByBookIdAndStatus(book.id(), BookLoan.LoanStatus.ACTIVE))
                    .isEqualTo(1);
            assertThat(bookService.getBookById(book.id()).availableCopies()).isZero();
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void migrationRejectsInvalidLoanDatesAndReturnState() {
        int id = IDS.incrementAndGet();
        BookResponseDto book = bookService.createBook(new BookRequestDto(
                "Database constraints", "Author", 2020, String.format("978%010d", id), 1));
        Reader reader = createReader(id);
        LocalDate today = LocalDate.now();
        String insert = "INSERT INTO book_loans "
                + "(book_id, reader_id, loan_date, due_date, return_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        assertThatThrownBy(() -> jdbcTemplate.update(insert, book.id(), reader.getId(),
                Date.valueOf(today), Date.valueOf(today.minusDays(1)), null, "ACTIVE"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> jdbcTemplate.update(insert, book.id(), reader.getId(),
                Date.valueOf(today), Date.valueOf(today.plusDays(7)), null, "RETURNED"))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private RuntimeException issueAfterStart(CountDownLatch start, Long bookId, Long readerId)
            throws InterruptedException {
        start.await();
        try {
            loanService.loanBook(new BookLoanRequestDto(bookId, readerId, LocalDate.now().plusDays(7)));
            return null;
        } catch (RuntimeException rejected) {
            return rejected;
        }
    }

    private Reader createReader(int id) {
        Reader reader = new Reader("Test", "Reader", "reader-" + id + "@example.test",
                String.format("+7999%07d", id));
        return readerRepository.save(reader);
    }
}
