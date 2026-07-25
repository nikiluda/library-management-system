package com.zhanlin.library_management_system.exceptions;

import org.springframework.http.HttpStatus;

public abstract class LibraryException extends RuntimeException {

    protected LibraryException(String message) {
        super(message);
    }

    public abstract HttpStatus getStatus();
}
