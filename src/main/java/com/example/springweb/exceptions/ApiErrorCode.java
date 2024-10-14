package com.example.springweb.exceptions;

import lombok.Getter;

@Getter
public enum ApiErrorCode {
    USER_NOT_FOUND("User not found"),
    APPOINTMENT_NOT_FOUND("UserAppointment not found"),
    PRODUCT_NOT_FOUND("Product not found"),
    TOKEN_NOT_FOUND("Token not found"),
    VALIDATION_ERROR("Validation error"),
    FORBIDDEN("No permission to perform this action."),
    UNAUTHORIZED("Login required to proceed."),
    INVALID_PASSWORD("Invalid password"),
    INVALID_TOKEN("Expired token"),
    WEATHER_DATA_NOT_FOUND("Weather data not found");




    private final String message;

    ApiErrorCode(String message) {
        this.message = message;
    }
}
