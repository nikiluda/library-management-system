package com.zhanlin.library_management_system.advice;


import com.zhanlin.library_management_system.exceptions.*;
import com.zhanlin.library_management_system.messages.ApiErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.core.AuthenticationException;
import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ProblemDetail createProblem(
            ErrorCode errorCode,
            String detail,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(errorCode.getStatus());

        problemDetail.setTitle(errorCode.getTitle());
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
                ApiErrorMessage.INTERNAL_ERROR.getMessage(),
                request
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException exception,
                                                      HttpServletRequest request) {

        return createProblem(
                ErrorCode.DATA_INTEGRITY_VIOLATION,
                ApiErrorMessage.DATA_INTEGRITY_VIOLATION.getMessage(),
                request
        );

    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException exception, HttpServletRequest request) {
        return createProblem(
                ErrorCode.VALIDATION_ERROR,
                ApiErrorMessage.VALIDATION_ERROR.getMessage(),
                request
        );

    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleOptimisticLock(ObjectOptimisticLockingFailureException exception, HttpServletRequest request) {

        return createProblem(
                ErrorCode.OPTIMISTIC_LOCK_CONFLICT,
                ApiErrorMessage.OPTIMISTIC_LOCK_CONFLICT.getMessage(),
                request
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthenticationException(
            AuthenticationException exception,
            HttpServletRequest request) {

        return createProblem(
                ErrorCode.INVALID_CREDENTIALS,
                ApiErrorMessage.INVALID_CREDENTIALS.getMessage(),
                request

        );
    }



}
