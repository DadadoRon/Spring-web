package com.example.springweb.repository;

import com.example.springweb.entity.UserAppointment;
import com.example.springweb.exceptions.UserAppointmentNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAppointmentRepository extends JpaRepository<UserAppointment, Integer> {

    List<UserAppointment> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);


    default UserAppointment findByIdRequired(Integer id) {
        return findById(id)
                .orElseThrow(() -> new UserAppointmentNotFoundException("UserAppointment not found with id: " + id));
    }

    default void checkIfExistsById(Integer id) {
        if (!existsById(id)) {
            throw new UserAppointmentNotFoundException("UserAppointment not found with id: " + id);
        }
    }
}
