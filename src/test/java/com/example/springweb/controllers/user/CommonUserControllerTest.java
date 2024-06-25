package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;

class CommonUserControllerTest extends BaseIntegrationTest {
    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testProfileAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testProfileAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testProfileAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_NOT_FOUND);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_NOT_FOUND);
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_NOT_FOUND);
    }

    @Test
    void testRegisterUserAsAdmin() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testRegisterUserAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK)
                .body("firstName", equalTo(newUser.getFirstName()));
    }

    @Test
    void testRegisterUserAsAnonymous() throws JsonProcessingException {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK);
        User newUser1 = UserModels.createUser(Role.USER);
        String json1 = objectMapper.writeValueAsString(newUser1);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json1)
                .post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK);
        User newUser2 = UserModels.createUser(Role.USER);
        String json2 = objectMapper.writeValueAsString(newUser2);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json2)
                .post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK);
    }
}