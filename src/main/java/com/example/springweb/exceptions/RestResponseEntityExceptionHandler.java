package com.example.springweb.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;



@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = { UserNotFoundException.class })
    protected ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.USER_NOT_FOUND.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.USER_NOT_FOUND.name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(value = { ProductNotFoundException.class })
    protected ResponseEntity<Object> handleProductNotFoundException(ProductNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.PRODUCT_NOT_FOUND.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.PRODUCT_NOT_FOUND.name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }

    @ExceptionHandler(value = { UserAppointmentNotFoundException.class })
    protected ResponseEntity<Object> handleUserAppointmentNotFoundException(UserAppointmentNotFoundException ex) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.APPOINTMENT_NOT_FOUND.getMessage())
                .debugMessage(ex.getMessage())
                .code(ApiErrorCode.APPOINTMENT_NOT_FOUND.name())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiError);
    }
}

