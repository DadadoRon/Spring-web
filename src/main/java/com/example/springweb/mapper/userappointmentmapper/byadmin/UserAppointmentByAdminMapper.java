package com.example.springweb.mapper.userappointmentmapper.byadmin;

import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAppointmentByAdminMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserAppointment toUserAppointmentForCreate(UserAppointmentByAdminCreateDto createDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    UserAppointment toUserAppointmentForUpdate(UserAppointmentByAdminUpdateDto updateDto);
}
