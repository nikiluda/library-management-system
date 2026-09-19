package com.zhanlin.library_management_system.specification;

import com.zhanlin.library_management_system.models.BookLoan;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LoanSpecifications {

    public static Specification<BookLoan> hasBookTitle(String bookTitle) {

        if (bookTitle == null || bookTitle.isBlank()) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("book").get("title")),
                                "%" + bookTitle.toLowerCase() + "%"

                );

    }

    public static Specification<BookLoan> hasReaderName(String readerName) {

        if (readerName==null || readerName.isBlank())
            return null;


        return (root, query, criteriaBuilder) ->
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("reader").get("firstName")),
                                "%" + readerName.toLowerCase() + "%"
                                ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("reader").get("lastName")),
                                "%" + readerName.toLowerCase() + "%"
                        )
                );
    }

    public static Specification<BookLoan> loanDateFrom(LocalDate date) {

        if (date == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("loanDate"),
                        date
                );
    }

    public static Specification<BookLoan> loanDateTo(LocalDate date) {

        if (date == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("loanDate"),
                        date
                );
    }

    public static Specification<BookLoan> dueDateFrom(LocalDate date) {

        if (date == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("dueDate"),
                        date
                );
    }

    public static Specification<BookLoan> dueDateTo(LocalDate date) {

        if (date == null) {
            return null;
        }

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("dueDate"),
                        date
                );
    }

    public static Specification<BookLoan> hasStatus(BookLoan.LoanStatus status) {

        if (status == null)
            return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }


}
