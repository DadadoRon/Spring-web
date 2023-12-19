package com.example.springweb.controllers.userAppointment;

import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserDto;
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
public class UserAppointmentCreateDto {
    private LocalDate date;
    private LocalTime time;
    private Integer userId;
    private Integer productId;
}
