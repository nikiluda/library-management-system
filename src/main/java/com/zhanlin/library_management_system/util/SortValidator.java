package com.zhanlin.library_management_system.util;

import com.zhanlin.library_management_system.exceptions.BusinessRuleException;
import com.zhanlin.library_management_system.exceptions.InvalidSortFieldException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class SortValidator {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "id",
            "title",
            "author",
            "publicationYear",
            "availableCopies"
    );

    private static final Set<String> ALLOWED_SORT_FIELDS_LOANS = Set.of(
            "id",
            "loanDate",
            "dueDate",
            "returnDate",
            "status",
            "book.title",
            "reader.firstName",
            "reader.lastName"
    );

    public void validate(Pageable pageable) {

        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();

            if (!ALLOWED_SORT_FIELDS.contains(property)) {
                throw new InvalidSortFieldException(
                        "Sorting by field '" + property + "' is not allowed"
                );
            }
        }
    }

    public void validateLoans(Pageable pageable) {

        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();

            if (!ALLOWED_SORT_FIELDS_LOANS.contains(property)) {
                throw new InvalidSortFieldException(
                        "Sorting by field '" + property + "' is not allowed"
                );
            }
        }
    }
}
