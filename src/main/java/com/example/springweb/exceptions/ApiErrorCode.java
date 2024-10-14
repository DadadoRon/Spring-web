package com.example.springweb.exceptions;

import lombok.Getter;

@Getter
public enum ApiErrorCode {
    USER_NOT_FOUND("User Not Found"),
    APPOINTMENT_NOT_FOUND("UserAppointment Not Found"),
    PRODUCT_NOT_FOUND("Product Not Found"),
    TOKEN_NOT_FOUND("Token not found"),
    VALIDATION_ERROR("Validation Error"),
    FORBIDDEN("No permission to perform this action."),
    UNAUTHORIZED("Login required to proceed."),
    INVALID_PASSWORD("Invalid Password"),
    INVALID_TOKEN("Expired Token");

    private final String message;

    ApiErrorCode(String message) {
        this.message = message;
    }
}
