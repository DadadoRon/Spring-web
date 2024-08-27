package com.example.springweb.controllers.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserResetPasswordDto(
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email
) { }
