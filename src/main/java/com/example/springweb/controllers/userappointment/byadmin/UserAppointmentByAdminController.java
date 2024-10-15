package com.example.springweb.controllers.userappointment.byadmin;

import com.example.springweb.controllers.ExistsDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.mapper.userappointmentmapper.UserAppointmentMapper;
import com.example.springweb.mapper.userappointmentmapper.byadmin.UserAppointmentByAdminMapper;
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
    private final UserAppointmentMapper userAppointmentMapper;

    @GetMapping
    public List<UserAppointmentDto> findAll() {
        return userAppointmentService.getAllUserAppointments().stream()
            .map(userAppointmentMapper::toDto)
            .toList();
    }

    @GetMapping("/{userId}")
    public ExistsDto existsByUserId(@PathVariable Integer userId) {
        boolean existsByUserId = userAppointmentService.checkIfExistsByUserId(userId);
        return new ExistsDto(existsByUserId);
    }

    @PostMapping
    public UserAppointmentDto create(@Valid @RequestBody UserAppointmentByAdminCreateDto createDto) {
        UserAppointment userAppointment = userAppointmentService.createUserAppointment(
            userAppointmentByAdminMapper.toUserAppointmentForCreate(createDto),
                createDto.userId(), createDto.productId()
        );
        return userAppointmentMapper.toDto(userAppointment);
    }

    @PutMapping
    public UserAppointmentDto update(@Valid @RequestBody UserAppointmentByAdminUpdateDto updateDto) {
        UserAppointment userAppointment = userAppointmentService.updateUserAppointment(
                userAppointmentByAdminMapper.toUserAppointmentForUpdate(updateDto));
        return userAppointmentMapper.toDto(userAppointment);
    }

    @DeleteMapping("/{id}")
    public void deleteUserAppointmentById(@PathVariable Integer id) {
        userAppointmentService.deleteUserAppointment(id);
    }
}
