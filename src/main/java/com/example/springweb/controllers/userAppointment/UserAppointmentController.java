package com.example.springweb.controllers.userAppointment;


import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.controllers.user.UserUpdateDto;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.mapper.UserAppointmentMapper;
import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserAppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static com.example.springweb.controllers.userAppointment.UserAppointmentController.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserAppointmentController {

    public static final String REQUEST_MAPPING = "/api/v1/user-appointments";

    private final UserAppointmentService userAppointmentService;
    private final UserAppointmentMapper userAppointmentMapper;

    @GetMapping
    public List<UserAppointmentDto> findAll() {
        return userAppointmentService.getAllUserAppointments().stream()
            .map(userAppointmentMapper::toDto)
            .toList();
    }

    @GetMapping("/personal")
    public List<UserAppointmentDto> findAllForUser() {
        Integer userId = UserContextHolder.getUser().getId();
        return userAppointmentService.getAllUserAppointmentsByUserId(userId).stream()
            .map(userAppointmentMapper::toDto)
            .sorted(Comparator.comparing(UserAppointmentDto::getDate))
            .toList();
    }

    @PostMapping
    public UserAppointmentDto create(@RequestBody UserAppointmentCreateDto createDto) {
        UserAppointment userAppointment = userAppointmentService.createUserAppointment(
            userAppointmentMapper.toUserAppointmentForCreate(createDto), createDto.getUserId(), createDto.getProductId()
        );
        return userAppointmentMapper.toDto(userAppointment);
    }

    @PutMapping
    public UserAppointmentDto update(@RequestBody UserAppointmentUpdateDto updateDto) {
        UserAppointment userAppointment = userAppointmentService.updateUserAppointment(
            userAppointmentMapper.toUserAppointmentForUpdate(updateDto));
        return userAppointmentMapper.toDto(userAppointment);
    }

    @DeleteMapping("/{id}")
    public void deleteUserAppointmentById(@PathVariable Integer id) {
        userAppointmentService.deleteUserAppointment(id);
    }

}
