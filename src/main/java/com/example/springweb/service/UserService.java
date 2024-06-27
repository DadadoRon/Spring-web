package com.example.springweb.service;

import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Integer userId);

    User getUserByEmail(String userEmail);

    User createUser(User user);

    User registerUser(User user);

    List<User> search(UserSearch search);

    User update(User user);

    void deleteUser(Integer userId);

}
