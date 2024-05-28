package com.example.springweb.controllers.userappointment;

import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserAppointmentDto {
    private Integer id;
    private ZonedDateTime dateTime;
    private UserDto user;
    private ProductDto product;
}

