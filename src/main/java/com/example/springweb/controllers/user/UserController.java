package com.example.springweb.controllers.user;

import com.example.springweb.entity.User;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.user.UserController.REQUEST_MAPPING;


@RestController
@Tag(name = "Users API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserController {

    public static final String REQUEST_MAPPING = "/api/v1/users";
    private final UserService userService;
    private final UserMapper userMapper;


    @GetMapping
    public List<UserDto> findAll() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/profile")
    public User findProfile() {
        return UserContextHolder.getUser();
    }

    @GetMapping("/{id}")
    public UserDto findAll(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return userMapper.toDto(user);
    }

    @PostMapping
    public UserDto create(@Valid @RequestBody  UserCreateDto createDto) {
        User user = userService.createUser(userMapper.toUserForCreate(createDto));
        return userMapper.toDto(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserUpdateDto updateDto) {
        User user = userService.update(userMapper.toUserForUpdate(updateDto));
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}



