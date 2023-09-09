package com.example.springweb;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


@RestController
public class UserController {

    @GetMapping("/users")
    public List<User> findAll() {
        return UserRepository.USERS;
    }

    @GetMapping("/users/{id}")
    public User findAll(@PathVariable Integer id) {
        return UserRepository.USERS.stream().filter(it -> it.getId().equals(id)).findAny().get();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        user.setId(ThreadLocalRandom.current().nextInt(1, 1000));
        UserRepository.USERS.add(user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        for (User tempUser : UserRepository.USERS) {
            if (tempUser.getId().equals(user.getId())) {
                tempUser.setEmail(user.getEmail());
                tempUser.setRole(user.getRole());
                tempUser.setPassword(user.getPassword());
                tempUser.setLastName(user.getLastName());
                tempUser.setFirstName(user.getFirstName());
                break;
            }
        }
        return user;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        UserRepository.USERS.removeIf((it)-> it.getId().equals(id));
    }
}

