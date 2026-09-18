package com.zhanlin.library_management_system.service;


import com.zhanlin.library_management_system.models.Book;
import com.zhanlin.library_management_system.models.Reader;
import com.zhanlin.library_management_system.repository.BookRepository;
import com.zhanlin.library_management_system.repository.ReaderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class BookLoanServiceImplTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void setUp() {
        transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Test
    void optimisticLocking_shouldPreventLostUpdate() {

        Book book = new Book(
                "Java Concurrency",
                "Joshua Bloch",
                "9780321349606",
                2008,
                1,
                1
        );

        book = bookRepository.save(book);
        Long bookId = book.getId();

        Reader reader1 = new Reader();
        reader1.setFirstName("Иван");
        reader1.setLastName("Иванов");
        reader1.setEmail("ivan@test.com");
        reader1.setPhone("+79990000001");

        Reader reader2 = new Reader();
        reader2.setFirstName("Пётр");
        reader2.setLastName("Петров");
        reader2.setEmail("petr@test.com");
        reader2.setPhone("+79990000002");

        reader1 = readerRepository.save(reader1);
        reader2 = readerRepository.save(reader2);


        CountDownLatch start = new CountDownLatch(1);

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch loaded = new CountDownLatch(2);

        Future<?> future1 = executor.submit(() -> transactionTemplate.execute(status -> {
            try {
                start.await();

                Book currentBook = bookRepository.findById(bookId)
                        .orElseThrow();

                loaded.countDown();
                loaded.await();

                currentBook.setAvailableCopies(
                        currentBook.getAvailableCopies() - 1
                );

                return null;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }));

        Future<?> future2 = executor.submit(() -> transactionTemplate.execute(status -> {
            try {
                start.await();

                Book currentBook = bookRepository.findById(bookId)
                        .orElseThrow();

                loaded.countDown();
                loaded.await();

                currentBook.setAvailableCopies(
                        currentBook.getAvailableCopies() - 1
                );

                return null;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            }
        }));

        start.countDown();

        boolean transaction1Failed = false;
        boolean transaction2Failed = false;

        try {
            future1.get();
        } catch (ExecutionException e) {
            transaction1Failed = true;
            assertInstanceOf(
                    ObjectOptimisticLockingFailureException.class,
                    e.getCause()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        try {
            future2.get();
        } catch (ExecutionException e) {
            transaction2Failed = true;

            assertInstanceOf(
                    ObjectOptimisticLockingFailureException.class,
                    e.getCause()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        assertTrue(transaction1Failed ^ transaction2Failed);
        executor.shutdown();
    }
}
