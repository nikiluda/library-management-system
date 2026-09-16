package com.zhanlin.library_management_system.exceptions;

public class BusinessRuleException extends LibraryException {
    public BusinessRuleException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
