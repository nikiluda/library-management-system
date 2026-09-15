package com.zhanlin.library_management_system.exceptions;

public class ActiveLoanExistsException extends RuntimeException {
    public ActiveLoanExistsException(String message) {
        super(message);
    }
}
