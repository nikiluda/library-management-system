package com.zhanlin.library_management_system.config;

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

    public void validate(Pageable pageable) {

        for (Sort.Order order : pageable.getSort()) {
            String property = order.getProperty();

            if (!ALLOWED_SORT_FIELDS.contains(property)) {
                throw new IllegalArgumentException(
                        "Sorting by field '" + property + "' is not allowed"
                );
            }
        }
    }
}
