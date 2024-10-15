package com.example.springweb.mapper.userappointmentmapper.byuser;

import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserCreateDto;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserUpdateDto;
import com.example.springweb.entity.UserAppointment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserAppointmentByUserMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "id", ignore = true)
    UserAppointment toUserAppointmentForCreate(UserAppointmentByUserCreateDto createDto);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product", ignore = true)
    UserAppointment toUserAppointmentForUpdate(UserAppointmentByUserUpdateDto updateDto);
}
