package com.example.springweb.exceptions;

import lombok.Getter;

@Getter
public class ExternalServiceException extends RuntimeException {
    private final ApiErrorCode code;

    public ExternalServiceException(String message, ApiErrorCode code) {
        super(message);
        this.code = code;
    }
}
