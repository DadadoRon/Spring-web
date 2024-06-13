package com.example.springweb.controllers.userappointment.byadmin;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.ZonedDateTime;

public record UserAppointmentByAdminCreateDto(
        @NotNull(message = "Date cannot be empty")
        @FutureOrPresent(message = "Date should be in the present or future")
        ZonedDateTime dateTime,
                @NotNull(message = "UserId cannot be empty")
        Integer userId,
        @NotNull(message = "ProductId cannot be empty")
        Integer productId
) {}


