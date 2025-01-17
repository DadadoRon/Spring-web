package com.example.springweb.controllers.user;

import com.example.springweb.annotations.ValidationConstants;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserRegisterDto(
        @NotEmpty(message = "First name cannot be empty")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters long")
        String firstName,
        @NotEmpty(message = "Last name cannot be empty")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters long")
        String lastName,
        @NotEmpty(message = "Email cannot be empty")
        @Email(message = "Email should be valid")
        String email,
        @NotEmpty(message = "Password cannot be empty")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(
                regexp = ValidationConstants.PASSWORD_REGEXP,
                message = ValidationConstants.PASSWORD_MESSAGE
        )
        String password
) { }
