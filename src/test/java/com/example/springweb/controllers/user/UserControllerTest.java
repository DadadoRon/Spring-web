package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;

class UserControllerTest extends BaseIntegrationTest {
    private TestUserDto user;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        user = createUser();
    }

    @Test
    void testUpdateUserPasswordAsAdmin() throws JsonProcessingException {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomAlphabetic(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .put(UserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testUpdateUserByIdAsUser() throws JsonProcessingException {
        String oldPassword = user.password();
        System.out.println(oldPassword);
        String newPassword = (RandomStringUtils.randomNumeric(8) + "@@");
        System.out.println(newPassword);
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        System.out.println(json);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .put(UserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testUpdateUserByIdAsAnonymous() throws JsonProcessingException {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomAlphabetic(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .put(UserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .put(UserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .put(UserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}