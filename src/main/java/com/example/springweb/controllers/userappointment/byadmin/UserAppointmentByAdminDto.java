package com.example.springweb.controllers.userappointment.byadmin;

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
public class UserAppointmentByAdminDto {
    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private UserDto user;
    private ProductDto product;
}
