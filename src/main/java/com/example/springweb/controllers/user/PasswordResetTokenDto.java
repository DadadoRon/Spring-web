package com.example.springweb.controllers.user;

import jakarta.validation.constraints.Pattern;

public record PasswordResetTokenDto(
        String token,
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[@$!%*?&])[\\d@$!%*?&]+$",
                message = "Password must contain at least one digit and one special character"
        )
        String newPassword
) { }
