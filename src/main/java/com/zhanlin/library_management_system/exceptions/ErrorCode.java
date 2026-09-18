package com.zhanlin.library_management_system.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "Book not found"),
    READER_NOT_FOUND(HttpStatus.NOT_FOUND, "Reader not found"),
    LOAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Loan not found"),

    ISBN_ALREADY_EXISTS(HttpStatus.CONFLICT, "ISBN already exists"),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "Email already exists"),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT, "Phone already exists"),

    NO_AVAILABLE_COPIES(HttpStatus.CONFLICT, "No available copies"),
    ACTIVE_LOAN_EXISTS(HttpStatus.CONFLICT, "Active loan exists"),
    LOAN_ALREADY_RETURNED(HttpStatus.CONFLICT, "Loan already returned"),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "Data integrity violation"),
    MALFORMED_REQUEST(HttpStatus.BAD_REQUEST, "Malformed request"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed"),

    INVALID_SORT_FIELD(HttpStatus.BAD_REQUEST, "Invalid sort field"),

    OPTIMISTIC_LOCK_CONFLICT(HttpStatus.CONFLICT, "Optimistic locking conflict"),
    ;

    private final HttpStatus status;
    private final String title;

    ErrorCode(HttpStatus status, String title) {
        this.status = status;
        this.title = title;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }
}
