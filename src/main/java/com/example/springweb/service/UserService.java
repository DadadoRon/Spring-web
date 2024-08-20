package com.example.springweb.service;

import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Integer userId);

    User getUserByEmail(String userEmail);

    User createUser(User user);

    User registerUser(User user);

    List<User> search(UserSearch search);

    User update(User user);

    @CachePut(key = "#userId")
    @CacheEvict(allEntries = true)
    void updatePassword(Integer userId, String oldPassword, String newPassword);

    void deleteUser(Integer userId);

}
