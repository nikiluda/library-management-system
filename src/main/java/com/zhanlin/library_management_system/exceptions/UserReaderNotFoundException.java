package com.zhanlin.library_management_system.exceptions;

import com.zhanlin.library_management_system.messages.ApiErrorMessage;

public class UserReaderNotFoundException extends LibraryException {
    public UserReaderNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
