package com.example.springweb.controllers.user;

import com.example.springweb.annotations.ValidationConstants;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateDtoByUser(
        @Pattern(
                regexp = ValidationConstants.PASSWORD_REGEXP,
                message = ValidationConstants.PASSWORD_MESSAGE
        )
        String oldPassword,

        @Pattern(
                regexp = ValidationConstants.PASSWORD_REGEXP,
                message = ValidationConstants.PASSWORD_MESSAGE
        )
        String newPassword
) { }




