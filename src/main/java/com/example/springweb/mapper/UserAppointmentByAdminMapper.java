package com.example.springweb.mapper;

import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAppointmentByAdminMapper {
    UserAppointmentDto toDto(UserAppointment userAppointment);
    UserAppointment toUserAppointmentForCreate(UserAppointmentByAdminCreateDto createDto);
    UserAppointment toUserAppointmentForUpdate(UserAppointmentByAdminUpdateDto updateDto);
}
