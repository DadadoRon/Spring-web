package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AdminUserControllerTest extends BaseIntegrationTest {

    private static List<UserDto> userList = new ArrayList<>();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        userList = createUsers();
    }

    @Test
    void testGetUserByIdAsAdmin() {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(userId));
    }

    @Test
    void testGetUserByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testGetUserByIdAsAnonymous() {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testGetAllUsersAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(userRepository.findAll().size()));
    }

    @Test
    void testGetAllUsersAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testGetAllUsersAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testUpdateUserByIdAsAdmin() throws JsonProcessingException {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
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
                .put(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(userId))
                .body("firstName", equalTo(newFirstName));
    }

    @Test
    void testUpdateUserByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
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
                .put(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testUpdateUserByIdAsAnonymous() throws JsonProcessingException {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
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
                .put(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .put(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .put(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testCreateUserAsAdmin() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK)
                .body("firstName", equalTo(newUser.getFirstName()));
    }

    @Test
    void testCreateUserAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testCreateUserAsAnonymous() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }


    @Test
    void testDeleteUserAsAdmin() throws JsonProcessingException {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        assertTrue(userRepository.existsById(userId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testDeleteUserAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        assertTrue(userRepository.existsById(userId));
        userRepository.deleteById(userId);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testDeleteUserAsAnonymous() {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        userRepository.deleteById(userId);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%d", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .delete(String.format("%s/%d", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format("%s/%d", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testCacheAfterUpdate() throws JsonProcessingException {
        List<User> usersBeforeUpdate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", User.class);
        Integer userId = userList.get(getRandomIndex(userList.size())).getId();
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
                .put(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(userId))
                .body("firstName", equalTo(newFirstName));
        List<User> usersAfterUpdate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", User.class);
        assertNotEquals(usersBeforeUpdate, usersAfterUpdate);
    }

    @Test
    void testCacheAfterCreate() throws JsonProcessingException {
        List<User> usersBeforeCreate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", User.class);
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK)
                .body("firstName", equalTo(newUser.getFirstName()));
        List<User> usersAfterreate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", User.class);
        assertNotEquals(usersBeforeCreate, usersAfterreate);
    }

    @Test
    void testCacheAfterDelete() {
        List<User> usersBeforeDelete = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", User.class);
        Integer userId = userList.get(getRandomIndex(userList.size())).getId();
        assertTrue(userRepository.existsById(userId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .then()
                .statusCode(SC_OK);
        List<User> usersAfterDelete = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(AdminUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", User.class);
        assertNotEquals(usersBeforeDelete, usersAfterDelete);
    }
}

