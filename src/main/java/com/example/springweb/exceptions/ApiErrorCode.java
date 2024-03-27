package com.example.springweb.exceptions;

import lombok.Getter;

@Getter
public enum ApiErrorCode {
    USER_NOT_FOUND("User Not Found"),
    APPOINTMENT_NOT_FOUND("UserAppointment Not Found"),
    PRODUCT_NOT_FOUND("Product Not Found"),
    VALIDATION_ERROR("Validation Error"),
    FORBIDDEN("Required role is missing to perform this action."),
    UNAUTHORIZED("Login required to proceed.");



    private final String message;

    ApiErrorCode(String message) {
        this.message = message;
    }
}
