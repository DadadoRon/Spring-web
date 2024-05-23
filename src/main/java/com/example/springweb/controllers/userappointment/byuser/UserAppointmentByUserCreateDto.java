package com.example.springweb.controllers.userappointment.byuser;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAppointmentByUserCreateDto {
    @NotNull(message = "Date cannot be empty")
    @FutureOrPresent(message = "Date should be in the present or future")
    private LocalDate date;
    @NotNull(message = "Time cannot be empty")
    private LocalTime time;
    @NotNull(message = "ProductId cannot be empty")
    private Integer productId;
}
