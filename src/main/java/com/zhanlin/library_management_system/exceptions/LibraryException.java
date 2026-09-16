package com.zhanlin.library_management_system.exceptions;


public abstract class LibraryException extends RuntimeException {

    private final ErrorCode errorCode;


    protected LibraryException(ErrorCode errorCode, String message) {

        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {

        return errorCode;
    }

}
