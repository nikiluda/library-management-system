package com.zhanlin.library_management_system.specification;

import com.zhanlin.library_management_system.models.Book;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    public static Specification<Book> hasTitle(String title) {


        if (title == null || title.isBlank())
            return null;


        return (root, query, criteriaBuilder) ->

          criteriaBuilder.like(
                  criteriaBuilder.lower(root.get("title")),
                  "%" + title.toLowerCase() + "%"

          );

    }

    public static Specification<Book> hasAuthor(String author) {


        if (author == null || author.isBlank())
            return null;


        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("author")),
                        "%" + author.toLowerCase() + "%"
                );
    }

    public static Specification<Book> hasIsbn(String isbn) {

        if (isbn == null || isbn.isBlank())
            return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("isbn"),
                        isbn
                );
    }

    public static Specification<Book> hasPublicationYear(Integer year) {

        if (year == null)
            return null;

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("publicationYear"),
                        year
                );
    }

    public static Specification<Book> isAvailable(Boolean available) {

        if (available == null)
            return null;

        if (!available) {
            return (root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(
                            root.get("availableCopies"),0
                    );
        }
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(
                        root.get("availableCopies"),
                        0
                );
    }
}
