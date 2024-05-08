package com.example.springweb.mapper;

import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAppointmentByAdminMapper {
    UserAppointmentByAdminDto toDto(UserAppointment userAppointment);
    UserAppointment toUserAppointmentForCreate(UserAppointmentByAdminCreateDto createDto);
    UserAppointment toUserAppointmentForUpdate(UserAppointmentByAdminUpdateDto updateDto);



}
