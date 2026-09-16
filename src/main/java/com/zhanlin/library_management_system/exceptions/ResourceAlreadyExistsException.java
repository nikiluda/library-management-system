package com.zhanlin.library_management_system.exceptions;

public class ResourceAlreadyExistsException extends LibraryException {
    public ResourceAlreadyExistsException(
            ErrorCode errorCode,
            String message
    ) {
        super(errorCode, message);
    }
}
