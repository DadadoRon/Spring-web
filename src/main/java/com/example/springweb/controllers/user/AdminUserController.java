package com.example.springweb.controllers.user;

import com.example.springweb.entity.User;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.user.AdminUserController.REQUEST_MAPPING;


@RestController
@Tag(name = "Users API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class AdminUserController {
    public static final String REQUEST_MAPPING = "/api/v1/admin/users";
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> findAll() {
        return userService.getAllUsers().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public UserDto findAll(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        return userMapper.toDto(user);
    }

    @PostMapping("/create")
    public UserDto create(@Valid @RequestBody UserCreateDto createDto) {
        User user = userService.createUser(userMapper.toUserForCreate(createDto));
        return userMapper.toDto(user);
    }

    @PostMapping("/search")
    public List<UserDto> search(@RequestBody UserSearchDto searchDto) {
        return userService.search(userMapper.toUserForSearch(searchDto)).stream()
                .map(userMapper::toDto)
                .toList();
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



