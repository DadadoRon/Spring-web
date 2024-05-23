package com.example.springweb.exceptions;

public class ForbiddenUserException extends RuntimeException{

    public ForbiddenUserException(String message) {
        super(message);
    }
}
