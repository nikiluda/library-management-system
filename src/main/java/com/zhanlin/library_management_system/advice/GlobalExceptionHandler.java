package com.zhanlin.library_management_system.advice;


import com.zhanlin.library_management_system.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail createProblem(
            HttpStatus status,
            String title,
            String detail,
            ErrorCode errorCode,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);

        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setInstance(
                URI.create(request.getRequestURI())
        );
        problemDetail.setProperty(
                "code",
                errorCode.name()
        );

        return problemDetail;
    }

    @ExceptionHandler(LibraryException.class)
    public ProblemDetail handleLibraryException(LibraryException exception, HttpServletRequest request) {

        ErrorCode errorCode = exception.getErrorCode();

        return createProblem(
                errorCode.getStatus(),
                "Library error",
                exception.getMessage(),
                errorCode,
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = createProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more fields are invalid",
                ErrorCode.VALIDATION_ERROR,
                request
        );

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
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        return createProblem(
                HttpStatus.BAD_REQUEST,
                "Malformed request",
                "Request body is invalid",
                ErrorCode.MALFORMED_REQUEST,
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        return createProblem(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal server error",
                "An unexpected error occurred",
                ErrorCode.INTERNAL_ERROR,
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception,
                                                      HttpServletRequest request) {

        return createProblem(
                HttpStatus.CONFLICT,
                "Data integrity violation",
                "The request conflicts with existing data",
                ErrorCode.DATA_INTEGRITY_VIOLATION,
                request
        );

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request) {
        return createProblem(
                HttpStatus.BAD_REQUEST,
                "Validation failed",
                "One or more parameters are invalid",
                ErrorCode.VALIDATION_ERROR,
                request
        );

    }



}
