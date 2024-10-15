package com.example.springweb.repository;

import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserRepositoryTest {
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private UserRepository userRepository;

    private final User user = UserModels.createUser(Role.USER);

    private final List<User> userList = UserModels.getRandomUserList();

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setUp() {
        userRepository.saveAll(userList);
    }

    @AfterEach
    void delete() {
        userRepository.deleteAll();
    }

    @Test
    void findAllTest() {
        assertEquals(userRepository.findAll().size(), userList.size());
    }

    @Test
    void findByEmailTest() {
        userRepository.save(user);
        String userEmail = user.getEmail();
        Optional<User> byEmail = userRepository.findByEmail(userEmail);
        assertTrue(byEmail.isPresent());
    }

    @Test
    void findByIdTest() {
        userRepository.save(user);
        Integer userId = user.getId();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
    }

    @Test
    void saveTest() {
        assertEquals(userRepository.findAll().size(), userList.size());
    }

    @Test
    void updateTest() {
        userRepository.save(user);
        Integer userId = user.getId();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        updatedUser.setFirstName("Tanya");
        userRepository.save(updatedUser);
        assertEquals(userRepository.findAll().size(), userList.size() + 1);
        assertEquals("Tanya", updatedUser.getFirstName());
    }

    @Test
    void deleteTest() {
        userRepository.save(user);
        Integer userId = user.getId();
        userRepository.deleteById(userId);
        assertEquals(userRepository.findAll().size(), userList.size());
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
}