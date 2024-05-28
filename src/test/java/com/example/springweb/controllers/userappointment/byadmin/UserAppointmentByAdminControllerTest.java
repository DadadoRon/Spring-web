package com.example.springweb.controllers.userappointment.byadmin;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserAppointmentModels;
import com.example.springweb.UserModels;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.UserAppointment;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserAppointmentByAdminControllerTest extends BaseIntegrationTest {

    private List<UserAppointmentDto> userAppointmentList = new ArrayList<>();

    private UserDto user = new UserDto();

    private ProductDto product = new ProductDto();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        admin = UserModels.createUser(Role.ADMIN);
        userRepository.save(admin);
        user = createUser();
        product = createProduct();
        userAppointmentList = createUserAppointments(user.getId(), product.getId());
    }

    @Test
    void testGetAllUserAppointmentsAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(userAppointmentRepository.findAll().size()));
    }

    @Test
    void testGetAllUserAppointmentsAsUser() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testGetAllUserAppointmentsAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testUpdateUserAppointmentByIdAsAdmin() throws com.fasterxml.jackson.core.JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        LocalDate newDate = LocalDate.now();
        updatedUserAppointment.setDate(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(updatedUserAppointment.getId()))
                .body("date", equalTo(updatedUserAppointment.getDate().toString()));
    }

    @Test
    void testUpdateUserAppointmentByIdAsUser() throws JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        LocalDate newDate = LocalDate.now();
        updatedUserAppointment.setDate(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testUpdateUserAppointmentByIdAsAnonymous() throws JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        LocalDate newDate = LocalDate.now();
        updatedUserAppointment.setDate(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testCreateUserAppointmentAsAdmin() throws JsonProcessingException {
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.getId(), product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testCreateUserAppointmentAsUser() throws JsonProcessingException {
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.getId(), product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testCreateUserAppointmentAsAnonymous() throws JsonProcessingException {
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.getId(), product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testDeleteUserAppointmentAsAdmin() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testDeleteUserAppointmentAsUser() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testDeleteUserAppointmentAsAnonymous() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .delete(String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}