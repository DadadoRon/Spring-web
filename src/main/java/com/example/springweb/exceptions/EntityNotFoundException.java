package com.example.springweb.exceptions;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final ApiErrorCode code;

    public EntityNotFoundException(String message, ApiErrorCode code) {
        super(message);
        this.code = code;
    }
}
