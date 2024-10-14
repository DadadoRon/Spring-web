package com.example.springweb.exceptions.handler;

import com.example.springweb.exceptions.ApiError;
import com.example.springweb.exceptions.ApiErrorCode;
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
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
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
}
