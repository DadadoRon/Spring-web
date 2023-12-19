package com.example.springweb.repository;

import com.example.springweb.entity.UserAppointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAppointmentRepository extends JpaRepository<UserAppointment, Integer> {

    List<UserAppointment> findByUserId(Integer userId);



}
