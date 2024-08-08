package com.example.springweb.mapper.userappointmentmapper;

import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAppointmentMapper {
    @Mapping(target = "user", source = "user")
    @Mapping(target = "product", source = "product")
    UserAppointmentDto toDto(UserAppointment userAppointment);
}
