package com.example.springweb.exceptions.handler;

import com.example.springweb.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<ApiError> handleUserNotFoundException(EntityNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ex.getCode().getMessage())
                .debugMessage(ex.getMessage())
                .code(ex.getCode().name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(value = {ForbiddenUserException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(ForbiddenUserException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.FORBIDDEN.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.FORBIDDEN.name())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiError);
    }

    @ExceptionHandler(value = {InvalidPasswordException.class})
    protected ResponseEntity<Object> handleAccessDeniedException(InvalidPasswordException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.INVALID_PASSWORD.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.INVALID_PASSWORD.name())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(value = {TokenNotFoundException.class})
    protected ResponseEntity<Object> handleTokenNotFoundException(TokenNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.TOKEN_NOT_FOUND.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.TOKEN_NOT_FOUND.name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    protected ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.INVALID_TOKEN.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.INVALID_TOKEN.name())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiError);
    }

    @ExceptionHandler(value = {ExternalServiceException.class})
    protected ResponseEntity<Object> handleWeatherDataNotFoundException(ExternalServiceException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.WEATHER_DATA_NOT_FOUND.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.WEATHER_DATA_NOT_FOUND.name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}

