package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.models.BookLoan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long>, JpaSpecificationExecutor<BookLoan> {
    Page<BookLoan> findByReaderId(Long readerId, Pageable pageable);

    List<BookLoan> findByStatusAndDueDateBefore(
            BookLoan.LoanStatus status,
            LocalDate today
    );

    boolean existsByBookIdAndReaderIdAndStatus(
            Long bookId,
            Long readerId,
            BookLoan.LoanStatus status
    );

    Optional<BookLoan> findByIdAndReaderId(Long loanId, Long readerId);
}
