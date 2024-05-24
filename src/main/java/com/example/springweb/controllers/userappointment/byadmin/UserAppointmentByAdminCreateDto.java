package com.example.springweb.controllers.userappointment.byadmin;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAppointmentByAdminCreateDto {
        @NotNull(message = "Date cannot be empty")
        @FutureOrPresent(message = "Date should be in the present or future")
        private ZonedDateTime dateTime;
        @NotNull(message = "UserId cannot be empty")
        private Integer userId;
        @NotNull(message = "ProductId cannot be empty")
        private Integer productId;
}


