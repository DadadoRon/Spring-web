package com.example.springweb.service;



import com.example.springweb.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(Integer userId);
    User createUser(User user);
    User update(User user);
    void deleteUser(Integer userId);
}
