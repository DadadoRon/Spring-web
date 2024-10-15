package com.example.springweb.exceptions;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {

        super(message);
    }
}
