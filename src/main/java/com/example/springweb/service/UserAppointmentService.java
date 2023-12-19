package com.example.springweb.service;

import com.example.springweb.entity.UserAppointment;

import java.util.List;
import java.util.Optional;

public interface UserAppointmentService {
    List<UserAppointment> getAllUserAppointments();

    List<UserAppointment> getAllUserAppointmentsByUserId(Integer userId);

    UserAppointment createUserAppointment(UserAppointment userAppointment, Integer userId, Integer productId);
    UserAppointment updateUserAppointment(UserAppointment userAppointment);
    void deleteUserAppointment(Integer userAppointmentId);
}
