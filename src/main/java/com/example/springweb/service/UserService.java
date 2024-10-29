package com.example.springweb.service;

import com.example.springweb.entity.PasswordResetToken;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserSearch;
import com.example.springweb.exceptions.*;
import com.example.springweb.repository.PasswordTokenRepository;
import com.example.springweb.repository.UserRepository;
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
public class UserService extends BaseService<User, Integer> {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordTokenRepository passwordTokenRepository;

    public UserService(UserRepository userRepository, EmailService emailService,
                       PasswordTokenRepository passwordTokenRepository) {
        super(userRepository);
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @Cacheable(key = "#userId")
    public User findByIdRequired(Integer userId) {
        return userRepository.findByIdRequired(userId);
    }

    @Cacheable(key = "#userEmail")
    public User getUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email" + userEmail + "not found",
                        ApiErrorCode.USER_NOT_FOUND));
    }

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
    public User create(User user) {
        String salt = BCrypt.gensalt();
        String hashed = BCrypt.hashpw(user.getPassword(), salt);
        user.setSalt(salt);
        user.setPassword(hashed);
        return super.create(user);
    }

    public List<User> search(UserSearch search) {
        return userRepository.findAllByAnyFieldsIgnoreCaseContaining(search);
    }

    @Override
    public User update(User user) {
        Integer userId = user.getId();
        User userByIdRequired = userRepository.findByIdRequired(userId);
        user.setPassword(userByIdRequired.getPassword());
        user.setSalt(userByIdRequired.getSalt());
        return super.update(user);
    }

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
    public void delete(Integer userId) {
        userRepository.checkIfExistsById(userId);
        super.delete(userId);
    }

    public void resetPassword(String email) {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isEmpty()) {
            throw new EntityNotFoundException("User not found with email: " + email, ApiErrorCode.USER_NOT_FOUND);
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
        String subject = "Password Reset Request";
        emailService.sendPasswordResetEmail(email, subject, resetLink);

    }

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


