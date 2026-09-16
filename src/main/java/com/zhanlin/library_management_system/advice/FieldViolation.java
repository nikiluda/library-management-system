package com.zhanlin.library_management_system.advice;

public record FieldViolation(
        String field,
        String code,
        String message
) {

}
