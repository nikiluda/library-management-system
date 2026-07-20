package com.zhanlin.library_management_system.repository;

import com.zhanlin.library_management_system.models.BookLoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {
}
