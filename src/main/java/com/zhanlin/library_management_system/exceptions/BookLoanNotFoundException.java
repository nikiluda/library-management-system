package com.zhanlin.library_management_system.exceptions;

public class BookLoanNotFoundException extends RuntimeException {
    public BookLoanNotFoundException(String message) {
        super(message);
    }
}
