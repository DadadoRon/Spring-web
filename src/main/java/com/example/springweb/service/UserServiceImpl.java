package com.example.springweb.service;

import com.example.springweb.entity.Role;
import com.example.springweb.repository.UserRepository;
import com.example.springweb.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer userId) {

        return userRepository.findById(userId).get();
    }

    @Override
    public User createUser(User user) {
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.deleteById(userId);
    }
}

