package com.zhanlin.library_management_system.exceptions;

public class InvalidSortFieldException extends LibraryException {
    public InvalidSortFieldException(String message) {

        super(ErrorCode.INVALID_SORT_FIELD, message);
    }
}
