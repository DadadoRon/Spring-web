package com.example.springweb.controllers.user;

public record PasswordUpdateResponseDto(
        boolean success,
        String message
) { }
