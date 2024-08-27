package com.example.springweb.controllers.user;

import com.example.springweb.annotations.ValidationConstants;
import jakarta.validation.constraints.Pattern;

public record PasswordResetTokenDto(
        String token,
        @Pattern(
                regexp = ValidationConstants.PASSWORD_REGEXP,
                message = ValidationConstants.PASSWORD_MESSAGE
        )
        String newPassword
) { }
