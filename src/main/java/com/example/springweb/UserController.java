package com.example.springweb;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;


@RestController
public class UserController {

    @GetMapping("/users")
    public List<User> findAll() {

        return UserRepository.USERS;
    }

    @PostMapping("/users")
    public User create(@RequestBody User user) {
        UserRepository.USERS.add(user);
        return user;
    }

    @PutMapping("/users")
    public User update(@RequestBody User user) {
        int id = user.getId();
        User foundUser = null;
        for (User tempUser : UserRepository.USERS) {
            if (tempUser.getId() == id) {
                foundUser = tempUser;
                foundUser.setFirstName(user.getFirstName());
                foundUser.setLastName(user.getLastName());
            }
        }
        return foundUser;
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable Integer id) {
        UserRepository.USERS.removeIf((it)-> it.getId().equals(id));
        //for (User tempUser : UserRepository.USERS) {
            //if (tempUser.getId() == id) {
                 //User foundUser = tempUser;
                //UserRepository.USERS.remove(foundUser);
                //break;
            //}
    }
}

