package com.example.springweb.controllers.userAppointment;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UserAppointmentUpdateDto {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
}
