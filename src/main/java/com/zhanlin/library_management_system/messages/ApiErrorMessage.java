package com.zhanlin.library_management_system.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {

    BOOK_NOT_FOUND_BY_ID("Book with ID %s was not found");

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}