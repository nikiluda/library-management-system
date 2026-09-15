package com.zhanlin.library_management_system.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiMessage {

    BOOK_CREATED("Book successfully created"),
    BOOK_UPDATED("Book successfully updated"),

    READER_CREATED("Reader successfully created"),
    READER_UPDATED("Reader successfully updated"),

    BOOK_LOAN_CREATED("Book successfully loaned"),
    BOOK_RETURNED("Book successfully returned");
    ;

    private final String message;


}
