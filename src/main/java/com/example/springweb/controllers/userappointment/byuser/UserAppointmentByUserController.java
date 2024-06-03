package com.example.springweb.controllers.userappointment.byuser;


import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.exceptions.ForbiddenUserException;
import com.example.springweb.mapper.userappointmentmapper.UserAppointmentMapper;
import com.example.springweb.mapper.userappointmentmapper.byuser.UserAppointmentByUserMapper;
import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserAppointmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserController.REQUEST_MAPPING;

@RestController
@Tag(name = "UserAppointments API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserAppointmentByUserController {
    public static final String REQUEST_MAPPING = "/api/v1/user/user-appointments";
    private final UserAppointmentService userAppointmentService;
    private final UserAppointmentByUserMapper userAppointmentByUserMapper;
    private final UserAppointmentMapper userAppointmentMapper;

    @GetMapping
    public List<UserAppointmentDto> findAll() {
        Integer userId = UserContextHolder.getUser().getId();
        return userAppointmentService.getAllUserAppointmentsByUserId(userId).stream()
            .map(userAppointmentMapper::toDto)
            .sorted(Comparator.comparing(UserAppointmentDto::getDateTime))
            .toList();
    }

    @PostMapping
    public UserAppointmentDto create(@Valid @RequestBody UserAppointmentByUserCreateDto createDto) {
        Integer userId = UserContextHolder.getUser().getId();
        UserAppointment userAppointment = userAppointmentService.createUserAppointment(
            userAppointmentByUserMapper.toUserAppointmentForCreate(createDto), userId, createDto.getProductId()
        );
        return userAppointmentMapper.toDto(userAppointment);
    }

    @PutMapping
    public UserAppointmentDto update(@Valid @RequestBody UserAppointmentByUserUpdateDto updateDto) {
        Integer userId = UserContextHolder.getUser().getId();
        Integer userAppointmentId = updateDto.getId();
        UserAppointment userAppointmentById = userAppointmentService.getUserAppointmentById(userAppointmentId);
        Integer userIdFromUserAppointment = userAppointmentById.getUser().getId();
        if (!userId.equals(userIdFromUserAppointment)) {
            throw new ForbiddenUserException("You are not authorized to update this appointment");
        }
        UserAppointment userAppointment = userAppointmentService.updateUserAppointment(
                userAppointmentByUserMapper.toUserAppointmentForUpdate(updateDto));
        return userAppointmentMapper.toDto(userAppointment);
    }

    @DeleteMapping("/{id}")
    public void deleteUserAppointmentById(@PathVariable Integer id) {
        Integer userId = UserContextHolder.getUser().getId();
        UserAppointment userAppointmentById = userAppointmentService.getUserAppointmentById(id);
        Integer userIdFromUserAppointment = userAppointmentById.getUser().getId();
        if (!userId.equals(userIdFromUserAppointment)) {
            throw new ForbiddenUserException("You are not authorized to update this appointment");
        }
        userAppointmentService.deleteUserAppointment(id);
    }
}
