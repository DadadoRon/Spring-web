package com.example.springweb.controllers.user;

import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.entity.Product;
import com.example.springweb.entity.User;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.user.UserController.REQUEST_MAPPING;


@RestController
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

    @GetMapping("/{email}")
    public UserDto findAll(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        return userMapper.toDto(user);
    }

    @PostMapping
    public UserDto create(@RequestBody UserCreateDto createDto) {
        User user = userService.createUser(userMapper.toUserForCreate(createDto));
        return userMapper.toDto(user);
    }

    @PutMapping
    public UserDto update(@RequestBody UserUpdateDto updateDto) {
        User user = userService.update(userMapper.toUserForUpdate(updateDto));
        return userMapper.toDto(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

//    public UserDto toDto(User user) {
//        return UserDto.builder()
//                .id(user.getId())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .email(user.getEmail())
//                .password(user.getPassword())
//                .role(user.getRole())
//                .build();
//    }
//    public User toUserForCreate(UserCreateDto createDto) {
//        return User.builder()
//                .firstName(createDto.getFirstName())
//                .lastName(createDto.getLastName())
//                .email(createDto.getEmail())
//                .password(createDto.getPassword())
//                .role(createDto.getRole())
//                .build();
//    }
//    public User toUserForUpdate(UserUpdateDto updateDto) {
//        return User.builder()
//                .id(updateDto.getId())
//                .firstName(updateDto.getFirstName())
//                .lastName(updateDto.getLastName())
//                .email(updateDto.getEmail())
//                .password(updateDto.getPassword())
//                .role(updateDto.getRole())
//                .build();
//    }



}



