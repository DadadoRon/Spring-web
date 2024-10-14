package com.example.springweb.exceptions;

public class UserAppointmentNotFoundException extends RuntimeException {

    public UserAppointmentNotFoundException(String message) {
        super(message);
    }
}
