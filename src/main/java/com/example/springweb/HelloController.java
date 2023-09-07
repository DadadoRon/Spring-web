package com.example.springweb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/qq")
    public String hello(){

        return "Hello!";
    }

    @GetMapping("/name")
    public String name() {
        return "name!!";
    }

    @GetMapping("/user")
    public User findUser() {
        return new User(1, "Vadim", "Lazuk", "dad@mail.com", "124", Role.USER);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {

        return List.of(new User(1, "Vadim", "Lazuk", "dad@mail.com", "124", Role.USER),
                new User(2, "Dasha", "Dubik", "dad@mail.com", "124", Role.USER));
    }
}

// создать usercontroller с двумя методами - find random user, find all users
