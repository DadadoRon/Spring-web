package com.example.springweb.mapper.userappointmentmapper.byadmin;

import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAppointmentByAdminMapper {
    UserAppointment toUserAppointmentForCreate(UserAppointmentByAdminCreateDto createDto);
    UserAppointment toUserAppointmentForUpdate(UserAppointmentByAdminUpdateDto updateDto);
}
