package com.example.springweb.repository;

import com.example.springweb.entity.UserAppointment;
import com.example.springweb.exceptions.ApiErrorCode;

import java.util.List;

public interface UserAppointmentRepository extends BaseRepository<UserAppointment> {

    List<UserAppointment> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    @Override
    default String entityName() {
        return "UserAppointment";
    }

    @Override
    default ApiErrorCode notFoundErrorCode() {
        return ApiErrorCode.APPOINTMENT_NOT_FOUND;
    }
}
