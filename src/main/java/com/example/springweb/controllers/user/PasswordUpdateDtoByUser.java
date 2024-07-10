package com.example.springweb.controllers.user;

import jakarta.validation.constraints.Pattern;

public record PasswordUpdateDtoByUser(
        Integer id,
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[@$!%*?&])[\\d@$!%*?&]+$",
                message = "Password must contain at least one digit and one special character"
        )
        String OldPassword,

        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[@$!%*?&])[\\d@$!%*?&]+$",
                message = "Password must contain at least one digit and one special character"
        )
        String NewPassword
) {}




