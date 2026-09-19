package com.zhanlin.library_management_system.specification;

import com.zhanlin.library_management_system.models.BookLoan;
import org.springframework.data.jpa.domain.Specification;

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

//    public static Specification<BookLoan> hasReaderName(String readerName) {
//
//
//    }


}
