package com.example.springweb.service;

import com.example.springweb.entity.PasswordResetToken;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import com.example.springweb.exceptions.InvalidPasswordException;
import com.example.springweb.exceptions.InvalidTokenException;
import com.example.springweb.exceptions.TokenNotFoundException;
import com.example.springweb.exceptions.UserNotFoundException;
import com.example.springweb.repository.PasswordTokenRepository;
import com.example.springweb.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@CacheConfig(cacheNames = "users")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordTokenRepository passwordTokenRepository;

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
                .orElseThrow(() -> new UserNotFoundException("User with email"  + userEmail +  "not found"));
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
        if (!hashedPassword.equals(userById.getPassword())) {
           throw new InvalidPasswordException("Invalid old password");
        }
        userById.setPassword(BCrypt.hashpw(newPassword, salt));
        userRepository.save(userById);
    }

    @Override
    @CacheEvict(key = "#userId", allEntries = true)
    public void deleteUser(Integer userId) {
        userRepository.checkIfExistsById(userId);
        userRepository.deleteById(userId);
    }


    @Override
    public void resetPassword(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + email);
        }
        User user = byEmail.get();
        String token = UUID.randomUUID().toString();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(1);
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(expiryDate)
                .build();
        passwordTokenRepository.save(passwordResetToken);
        String resetLink = "http://localhost:8080/#/resetPassword?token=" + token;
        emailService.sendPasswordResetEmail(email, resetLink);
    }

    @Override
    public void initPassword(String token, String newPassword) {
        Optional<PasswordResetToken> byToken = passwordTokenRepository.findByToken(token);
        if (byToken.isEmpty()) {
            throw new TokenNotFoundException("Token not found");
        }
        PasswordResetToken passwordResetToken = byToken.get();
        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordTokenRepository.delete(passwordResetToken);
            throw new InvalidTokenException("Expired Token");
        }

        User user = passwordResetToken.getUser();
        String salt = user.getSalt();
        String hashedPassword = BCrypt.hashpw(newPassword, salt);
        user.setPassword(hashedPassword);
        passwordTokenRepository.delete(passwordResetToken);
    }
}


