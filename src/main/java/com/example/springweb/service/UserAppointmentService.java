package com.example.springweb.service;

import com.example.springweb.entity.UserAppointment;

import java.util.List;

public interface UserAppointmentService {
    List<UserAppointment> getAllUserAppointments();

    UserAppointment getUserAppointmentById(Integer userAppointmentId);

    List<UserAppointment> getAllUserAppointmentsByUserId(Integer userId);

    boolean checkIfExistsByUserId(Integer userId);

    UserAppointment createUserAppointment(UserAppointment userAppointment, Integer userId, Integer productId);

    UserAppointment updateUserAppointment(UserAppointment userAppointment);

    void deleteUserAppointment(Integer userAppointmentId);
}
