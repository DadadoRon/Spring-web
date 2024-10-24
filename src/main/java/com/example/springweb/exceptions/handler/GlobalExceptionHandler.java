package com.example.springweb.exceptions.handler;

import com.example.springweb.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.VALIDATION_ERROR.getMessage())
                .debugMessage(errors.get(0))
                .code(ApiErrorCode.VALIDATION_ERROR.name())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleUserNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ex.getCode().getMessage())
                .debugMessage(ex.getMessage())
                .code(ex.getCode().name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(value = {ForbiddenUserException.class})
    public ResponseEntity<ApiError> handleAccessDeniedException(ForbiddenUserException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.FORBIDDEN.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.FORBIDDEN.name())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(value = {InvalidPasswordException.class})
    public ResponseEntity<ApiError> handleAccessDeniedException(InvalidPasswordException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.INVALID_PASSWORD.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.INVALID_PASSWORD.name())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(value = {TokenNotFoundException.class})
    public ResponseEntity<ApiError> handleTokenNotFoundException(TokenNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.TOKEN_NOT_FOUND.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.TOKEN_NOT_FOUND.name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<ApiError> handleInvalidTokenException(InvalidTokenException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.INVALID_TOKEN.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.INVALID_TOKEN.name())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(value = {ExternalServiceException.class})
    public ResponseEntity<ApiError> handleWeatherDataNotFoundException(ExternalServiceException ex) {
        ApiError apiError = ApiError.builder()
                .message(ex.getCode().getMessage())
                .debugMessage(ex.getMessage())
                .code(ex.getCode().name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}
