package com.example.springweb.controllers.user;

import com.example.springweb.entity.User;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springweb.controllers.user.UserController.REQUEST_MAPPING;

@RestController
@Tag(name = "Users API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserController {
    public static final String REQUEST_MAPPING = "/api/v1/user/users";
    private final UserService userService;
    private final UserMapper userMapper;

    @PutMapping
    public UserDto updatePassword(@Valid @RequestBody PasswordUpdateDtoByUser passwordUpdateDtoByUser, String newPassword) {
        User user = userService.updatePassword(userMapper.toUserForUpdatePassword(passwordUpdateDtoByUser));
        return userMapper.toDto(user);
    }
}
