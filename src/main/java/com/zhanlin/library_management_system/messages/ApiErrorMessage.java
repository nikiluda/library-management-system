package com.zhanlin.library_management_system.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorMessage {

    BOOK_NOT_FOUND_BY_ID("Book with ID %s was not found"),
    READER_NOT_FOUND_BY_ID("Reader with ID %s was not found"),
    LOAN_NOT_FOUND_BY_ID("Loan with ID: %s was not found"),

    ISBN_ALREADY_EXISTS("Book with ISBN: %s already exists"),
    EMAIL_ALREADY_EXISTS("Reader with email: %s already exists"),
    PHONE_ALREADY_EXISTS("Reader with phone: %s already exists"),

    NO_AVAILABLE_COPIES("No available copies for book: %s"),
    ACTIVE_LOAN_EXISTS("Reader already has an active loan for this book"),
    LOAN_ALREADY_RETURNED("Loan with ID: %s has already been returned"),

    INVALID_SORT_FIELD("Invalid sort field"),

    VALIDATION_ERROR("One or more fields are invalid"),
    MALFORMED_REQUEST("Request body is invalid"),
    DATA_INTEGRITY_VIOLATION("The request conflicts with existing data"),
    INTERNAL_ERROR("An unexpected error occurred"),
    ;

    private final String message;

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}