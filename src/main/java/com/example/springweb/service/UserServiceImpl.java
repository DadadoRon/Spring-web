package com.example.springweb.service;

import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import com.example.springweb.exceptions.UserNotFoundException;
import com.example.springweb.repository.UserRepository;
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
        return userRepository.findByIdRequired(userId);
    }

    @Override
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new UserNotFoundException("User with email"  + userEmail +  "not found"));
    }
    @Override
    public User createUser(User user) {
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    public List<User> search(UserSearch search) {
        return userRepository.findAllByAnyFieldsIgnoreCaseContaining(search);
    }

    @Override
    public User update(User user) {
        Integer userId = user.getId();
        userRepository.checkIfExistsById(userId);
            return userRepository.save(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        userRepository.checkIfExistsById(userId);
        userRepository.deleteById(userId);
    }
}


