package com.example.springweb.controllers.userappointment.byadmin;


import com.example.springweb.entity.UserAppointment;
import com.example.springweb.mapper.UserAppointmentByAdminMapper;
import com.example.springweb.service.UserAppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminController.REQUEST_MAPPING;


@RestController
@Tag(name = "UserAppointments API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserAppointmentByAdminController {

    public static final String REQUEST_MAPPING = "/api/v1/admin/user-appointments";
    private final UserAppointmentService userAppointmentService;
    private final UserAppointmentByAdminMapper userAppointmentByAdminMapper;

    @GetMapping
    public List<UserAppointmentByAdminDto> findAll() {
        return userAppointmentService.getAllUserAppointments().stream()
            .map(userAppointmentByAdminMapper::toDto)
            .toList();
    }

    @PostMapping
    public UserAppointmentByAdminDto create(@Valid @RequestBody UserAppointmentByAdminCreateDto createDto) {
        UserAppointment userAppointment = userAppointmentService.createUserAppointment(
            userAppointmentByAdminMapper.toUserAppointmentForCreate(createDto), createDto.getUserId(), createDto.getProductId()
        );
        return userAppointmentByAdminMapper.toDto(userAppointment);
    }

    @PutMapping
    public UserAppointmentByAdminDto update(@Valid @RequestBody UserAppointmentByAdminUpdateDto updateDto) {
        UserAppointment userAppointment = userAppointmentService.updateUserAppointment(
                userAppointmentByAdminMapper.toUserAppointmentForUpdate(updateDto));
        return userAppointmentByAdminMapper.toDto(userAppointment);
    }

    @DeleteMapping("/{id}")
    public void deleteUserAppointmentById(@PathVariable Integer id) {
        userAppointmentService.deleteUserAppointment(id);
    }
}
