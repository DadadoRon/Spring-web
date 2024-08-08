package com.example.springweb.controllers.userappointment;

import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssX")
    private ZonedDateTime dateTime;
    private UserDto user;
    private ProductDto product;
}

