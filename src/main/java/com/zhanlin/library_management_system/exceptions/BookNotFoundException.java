package com.zhanlin.library_management_system.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends LibraryException{

    public BookNotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
