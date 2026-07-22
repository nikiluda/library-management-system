package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.models.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
    List<BookLoan> findByReaderId(Long readerId);

    List<BookLoan> findByStatusAndDueDateBefore(
            BookLoan.LoanStatus status,
            LocalDate today
    );
}
