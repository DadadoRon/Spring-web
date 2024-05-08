package com.example.springweb.controllers.user;

import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    @LocalServerPort
    private Integer port;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final String USERS_ENDPOINT = "/api/v1/users";

    private final User user = UserModels.createUser(Role.USER);
    private final User admin = UserModels.createUser(Role.ADMIN);
    private final User anonymous = UserModels.createUser(null);
    private final List<User> userList = UserModels.createRandomUserList();

    private Header getAuthorizationHeader(User user) {
        String authorizationHeaderValue = String.format("Basic %s", Base64.getEncoder().encodeToString(
                String.format("%s:%s", user.getEmail(), user.getPassword()).getBytes()));
        return new Header("Authorization", authorizationHeaderValue);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void setUp() {
        userRepository.save(admin);
        userRepository.save(user);
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void delete(){
        userRepository.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void getUserByIdAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/%s", USERS_ENDPOINT, user.getId()))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(user.getId()));
    }

    @Test
    void getUserByIdAsUser() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/%s", USERS_ENDPOINT, user.getId()))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void getUserByIdAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(String.format("%s/%s", USERS_ENDPOINT, user.getId()))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void getAllUsersAsAdmin() {
        userRepository.saveAll(userList);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(USERS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(userRepository.findAll().size()));
    }

    @Test
    void getAllUsersAsUser() {
        userRepository.saveAll(userList);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(USERS_ENDPOINT )
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void getAllUsersAsAnonymous() {
        userRepository.saveAll(userList);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(USERS_ENDPOINT )
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void profileAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/profile", USERS_ENDPOINT))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(admin.getId()));
    }

    @Test
    void profileAsUser() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/profile", USERS_ENDPOINT))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void profileAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(String.format("%s/profile", USERS_ENDPOINT))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void updateUserByIdAsAdmin() throws JsonProcessingException {
        Integer userId = user.getId();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        String newFirstName = RandomStringUtils.randomAlphabetic(8,12);
        updatedUser.setFirstName(newFirstName);
        String json = objectMapper.writeValueAsString(updatedUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .put(USERS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(userId))
                .body("firstName", equalTo(newFirstName));
    }

    @Test
    void updateUserByIdAsUser() throws JsonProcessingException {
        Integer userId = user.getId();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        updatedUser.setFirstName(RandomStringUtils.randomAlphabetic(8,12));
        String json = objectMapper.writeValueAsString(updatedUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .put(USERS_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateUserByIdAsAnonymous() throws JsonProcessingException {
        Integer userId = user.getId();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        updatedUser.setFirstName(RandomStringUtils.randomAlphabetic(8,12));
        String json = objectMapper.writeValueAsString(updatedUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .put(USERS_ENDPOINT)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void createUserAsAdmin() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(String.format("%s/create", USERS_ENDPOINT))
                .then()
                .statusCode(SC_OK)
                .body("firstName", equalTo(newUser.getFirstName()));
    }

    @Test
    void createUserAsUser() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(String.format("%s/create", USERS_ENDPOINT))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void createUserAsAnonymous() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(String.format("%s/create", USERS_ENDPOINT))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void registerUserAsAdmin() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(String.format("%s/register", USERS_ENDPOINT))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void registerUserAsUser() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(String.format("%s/register", USERS_ENDPOINT))
                .then()
                .statusCode(SC_OK)
                .body("firstName", equalTo(newUser.getFirstName()));
    }

    @Test
    void registerUserAsAnonymous() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(String.format("%s/register", USERS_ENDPOINT))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void deleteUserAsAdmin() {
        User newUser = UserModels.createUser(Role.USER);
        userRepository.save(newUser);
        Integer newUserId = newUser.getId();
        assertTrue(userRepository.existsById(newUserId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", USERS_ENDPOINT, newUserId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void deleteUserAsUser() {
        User newUser = UserModels.createUser(Role.USER);
        userRepository.save(newUser);
        Integer newUserId = newUser.getId();
        assertTrue(userRepository.existsById(newUserId));
        userRepository.deleteById(newUserId);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", USERS_ENDPOINT, newUserId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteUserAsAnonymous() {
        Integer userId = user.getId();
        userRepository.deleteById(userId);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%d", USERS_ENDPOINT, user.getId()))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}

