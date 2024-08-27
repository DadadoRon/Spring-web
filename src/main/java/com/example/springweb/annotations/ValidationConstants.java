package com.example.springweb.annotations;

public class ValidationConstants {
    public static final String PASSWORD_REGEXP = "^(?=.*\\d)(?=.*[@$!%*?&])[\\d@$!%*?&]+$";
    public static final String PASSWORD_MESSAGE = "Password must contain at least one digit and one special character";
}
