package com.example.springweb.controllers;

import com.example.springweb.entity.User;
import com.example.springweb.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/users")
    public List<User> findAll() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}")
    public User findAll(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
//        user.setId(ThreadLocalRandom.current().nextInt(1, 1000));
//        UserRepository.USERS.add(user);
//        return user;
        return userService.createUser(user);
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        userService.deleteUser(id);
    }
}

