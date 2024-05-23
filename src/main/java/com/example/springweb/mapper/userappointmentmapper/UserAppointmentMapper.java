package com.example.springweb.mapper.userappointmentmapper;

import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAppointmentMapper {
    UserAppointmentDto toDto(UserAppointment userAppointment);
}
