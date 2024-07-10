package com.example.springweb.service;

import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import com.example.springweb.exceptions.UserNotFoundException;
import com.example.springweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "users")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Cacheable
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Cacheable(key = "#userId")
    public User getUserById(Integer userId) {
        return userRepository.findByIdRequired(userId);
    }

    @Override
    @Cacheable(key = "#userEmail")
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new UserNotFoundException("User with email"  + userEmail +  "not found"));
    }
    @Override
    @CachePut(key = "#user.id")
    @CacheEvict(allEntries = true)
    public User registerUser(User user) {
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    @CachePut(key = "#user.id")
    @CacheEvict(allEntries = true)
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> search(UserSearch search) {
        return userRepository.findAllByAnyFieldsIgnoreCaseContaining(search);
    }

    @Override
    @CachePut(key = "#user.id")
    @CacheEvict(allEntries = true)
    public User update(User user) {
        Integer userId = user.getId();
        userRepository.checkIfExistsById(userId);
            return userRepository.save(user);
    }

    @Override
    @CachePut(key = "#user.id")
    @CacheEvict(allEntries = true)
    public User updatePassword(User user) {
        Integer userId = user.getId();
        User byIdRequired = userRepository.findByIdRequired(userId);
        BCrypt.checkpw(byIdRequired.getPassword(), user.getPassword());
        user.setFirstName(byIdRequired.getFirstName());
        user.setLastName(byIdRequired.getLastName());
        user.setEmail(byIdRequired.getEmail());
        user.setRole(byIdRequired.getRole());
        return userRepository.save(user);
    }

    @Override
    @CacheEvict(key = "#userId", allEntries = true)
    public void deleteUser(Integer userId) {
        userRepository.checkIfExistsById(userId);
        userRepository.deleteById(userId);
    }
}


