package com.zhanlin.library_management_system.advice;


import com.zhanlin.library_management_system.exceptions.*;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
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
            ErrorCode errorCode,
            String title,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getStatus());

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
                errorCode,
                "Library error",
                exception.getMessage(),
                request
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = createProblem(
                ErrorCode.VALIDATION_ERROR,
                "Validation failed",
                ApiErrorMessage.VALIDATION_ERROR.getMessage(),
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
                ErrorCode.MALFORMED_REQUEST,
                "Malformed request",
                ApiErrorMessage.MALFORMED_REQUEST.getMessage(),
                request
        );
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        return createProblem(
                ErrorCode.INTERNAL_ERROR,
                "Internal server error",
                ApiErrorMessage.INTERNAL_ERROR.getMessage(),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception,
                                                      HttpServletRequest request) {

        return createProblem(
                ErrorCode.DATA_INTEGRITY_VIOLATION,
                "Data integrity violation",
                ApiErrorMessage.DATA_INTEGRITY_VIOLATION.getMessage(),
                request
        );

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request) {
        return createProblem(
                ErrorCode.VALIDATION_ERROR,
                "Validation failed",
                ApiErrorMessage.VALIDATION_ERROR.getMessage(),
                request
        );

    }



}
