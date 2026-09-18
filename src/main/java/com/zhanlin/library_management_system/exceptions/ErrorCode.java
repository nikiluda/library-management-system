package com.zhanlin.library_management_system.exceptions;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND),
    READER_NOT_FOUND(HttpStatus.NOT_FOUND),
    LOAN_NOT_FOUND(HttpStatus.NOT_FOUND),

    ISBN_ALREADY_EXISTS(HttpStatus.CONFLICT),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    PHONE_ALREADY_EXISTS(HttpStatus.CONFLICT),

    NO_AVAILABLE_COPIES(HttpStatus.CONFLICT),
    ACTIVE_LOAN_EXISTS(HttpStatus.CONFLICT),
    LOAN_ALREADY_RETURNED(HttpStatus.CONFLICT),

    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT),
    MALFORMED_REQUEST(HttpStatus.BAD_REQUEST),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),

    INVALID_SORT_FIELD(HttpStatus.BAD_REQUEST),
    ;

    private final HttpStatus status;

    ErrorCode(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
