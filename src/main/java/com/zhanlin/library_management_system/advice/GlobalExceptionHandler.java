package com.zhanlin.library_management_system.advice;


import com.zhanlin.library_management_system.exceptions.BusinessRuleException;
import com.zhanlin.library_management_system.exceptions.ErrorCode;
import com.zhanlin.library_management_system.exceptions.ResourceAlreadyExistsException;
import com.zhanlin.library_management_system.exceptions.ResourceNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {



    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        problemDetail.setTitle("Resource not found");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty(
                "code",
                exception.getErrorCode().name()
        );

        return problemDetail;

    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ProblemDetail handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Resource already exists");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty(
                "code",
                exception.getErrorCode().name()
        );

        return problemDetail;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Business rule violation");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty(
                "code",
                exception.getErrorCode().name()
        );

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more fields are invalid");
        problemDetail.setProperty("code", ErrorCode.VALIDATION_ERROR.name());

        List<FieldViolation> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldViolation(
                        error.getField(),
                        error.getCode(),
                        error.getDefaultMessage()
                ))
                .toList();

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleMalformedRequest(
            HttpMessageNotReadableException exception
    ) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Malformed request");
        problemDetail.setDetail("Request body is invalid");
        problemDetail.setProperty("code", ErrorCode.MALFORMED_REQUEST.name());

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(Exception exception) {

        ProblemDetail problemDetail =
                ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        problemDetail.setTitle("Internal server error");
        problemDetail.setDetail("An unexpected error occurred");
        problemDetail.setProperty("code", ErrorCode.INTERNAL_ERROR.name());

        return problemDetail;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problemDetail.setTitle("Data integrity violation");
        problemDetail.setDetail("The request conflicts with existing data");
        problemDetail.setProperty("code", ErrorCode.DATA_INTEGRITY_VIOLATION.name());

        return problemDetail;

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problemDetail.setTitle("Validation failed");
        problemDetail.setDetail("One or more parameters are invalid");
        problemDetail.setProperty(
                "code",
                ErrorCode.VALIDATION_ERROR.name()
        );

        return problemDetail;
    }


}
