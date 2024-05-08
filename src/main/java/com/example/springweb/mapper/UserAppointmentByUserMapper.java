package com.example.springweb.mapper;

import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserCreateDto;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserDto;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAppointmentByUserMapper {
    UserAppointmentByUserDto toDto(UserAppointment userAppointment);
    UserAppointment toUserAppointmentForCreate(UserAppointmentByUserCreateDto createDto);
    UserAppointment toUserAppointmentForUpdate(UserAppointmentByUserUpdateDto updateDto);



}
