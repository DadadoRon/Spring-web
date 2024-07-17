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
        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(hashed);
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    @Override
    @CachePut(key = "#user.id")
    @CacheEvict(allEntries = true)
    public User createUser(User user) {
        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(hashed);
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
        User userByIdRequired = userRepository.findByIdRequired(userId);
        user.setPassword(userByIdRequired.getPassword());
        user.setSalt(userByIdRequired.getSalt());
        return userRepository.save(user);
    }

    @Override
    @CachePut(key = "#userId")
    @CacheEvict(allEntries = true)
    public void updatePassword(Integer userId, String oldPassword, String newPassword) {
        User userById = userRepository.findByIdRequired(userId);
        String salt = userById.getSalt();
        String hashedPassword = BCrypt.hashpw(oldPassword, salt);
        if (hashedPassword.equals(userById.getPassword())) {
            userById.setPassword(BCrypt.hashpw(newPassword, salt));
        } else {
            throw new IllegalArgumentException("Incorrect current password");
        }

        userRepository.save(userById);
    }

    @Override
    @CacheEvict(key = "#userId", allEntries = true)
    public void deleteUser(Integer userId) {
        userRepository.checkIfExistsById(userId);
        userRepository.deleteById(userId);
    }
}


