package com.zhanlin.library_management_system.exceptions;

public class ResourceNotFoundException extends LibraryException {


    public ResourceNotFoundException(
            ErrorCode errorCode, String message) {

        super(errorCode, message);
    }
}
