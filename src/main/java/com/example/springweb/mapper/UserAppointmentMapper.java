package com.example.springweb.mapper;

import com.example.springweb.controllers.userAppointment.UserAppointmentCreateDto;
import com.example.springweb.controllers.userAppointment.UserAppointmentDto;
import com.example.springweb.controllers.userAppointment.UserAppointmentUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAppointmentMapper {
    UserAppointmentDto toDto(UserAppointment userAppointment);
    UserAppointment toUserAppointmentForCreate(UserAppointmentCreateDto createDto);
    UserAppointment toUserAppointmentForUpdate(UserAppointmentUpdateDto updateDto);



}
