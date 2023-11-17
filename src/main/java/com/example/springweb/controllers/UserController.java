package com.example.springweb.controllers;

import com.example.springweb.entity.User;
import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.springweb.controllers.UserController.REQUEST_MAPPING;


@RestController
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserController {

    public static final String REQUEST_MAPPING = "/api/v1/users";

    private final UserService userService;


    @GetMapping
    public List<User> findAll() {

        return userService.getAllUsers();
    }

    @GetMapping("/profile")
    public User findProfile() {

        return UserContextHolder.getUser();
    }

    @GetMapping("/{id}")
    public User findAll(@PathVariable Integer id) {

        return userService.getUserById(id);
    }

    @GetMapping("/{email}")
    public User findAll(@PathVariable String email) {

        return userService.getUserByEmail(email);
    }

    @PostMapping
    public User create(@RequestBody User user) {
//        user.setId(ThreadLocalRandom.current().nextInt(1, 1000));
//        UserRepository.USERS.add(user);
//        return user;
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}

