package com.zhanlin.library_management_system.exceptions;

public enum ErrorCode {

    BOOK_NOT_FOUND,
    READER_NOT_FOUND,
    LOAN_NOT_FOUND,

    ISBN_ALREADY_EXISTS,
    EMAIL_ALREADY_EXISTS,

    NO_AVAILABLE_COPIES,
    ACTIVE_LOAN_EXISTS,
    LOAN_ALREADY_RETURNED
}
