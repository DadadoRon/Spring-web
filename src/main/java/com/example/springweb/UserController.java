package com.example.springweb;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class UserController {
    private static final List<User> USERS = new ArrayList<>(List.of(
        User.builder().id(1).email("1@mail.com").firstName("Ivan1").lastName("Petrov1").password("123").role(Role.ADMIN).build(),
        User.builder().id(2).email("2@mail.com").firstName("Ivan2").lastName("Petrov2").password("321").role(Role.USER).build(),
        User.builder().id(3).email("3@mail.com").firstName("Ivan3").lastName("Petrov3").password("456").role(Role.USER).build(),
        User.builder().id(4).email("4@mail.com").firstName("Ivan4").lastName("Petrov4").password("765").role(Role.USER).build(),
        User.builder().id(5).email("5@mail.com").firstName("Ivan5").lastName("Petrov5").password("789").role(Role.USER).build()
    ));

    @GetMapping("/users")
    public List<User> findAll() {
        return USERS;
    }

    @GetMapping("/users/{id}")
    public User findAll(@PathVariable Integer id) {
        return USERS.stream().filter(it -> it.getId().equals(id)).findAny().get();
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        user.setId(ThreadLocalRandom.current().nextInt(1, 1000));
        USERS.add(user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        System.out.println("update");
        for (User tempUser : USERS) {
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
    public void delete(@PathVariable Integer id) {
        USERS.removeIf(it -> it.getId().equals(id));
    }
}
