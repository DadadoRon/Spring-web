package com.example.springweb.controllers.userappointment.byuser;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserAppointmentModels;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserAppointmentByUserControllerTest extends BaseIntegrationTest {

    private List<UserAppointmentDto> userAppointmentList = new ArrayList<>();
    private ProductDto product = new ProductDto();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        userRepository.save(admin);
        product = createProduct();
        userAppointmentList = createUserAppointmentList(product.getId());
//        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void getAllUserAppointmentsAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void getAllUserAppointmentsAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void getAllUserAppointmentsAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void updateUserAppointmentByIdAsUser() throws JsonProcessingException {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        UserDto user = userAppointment.getUser();
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
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(updatedUserAppointment.getId()))
                .body("date", equalTo(updatedUserAppointment.getDate().toString()));
    }

    @Test
    void updateUserAppointmentByIdAsSomeUser() throws JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        LocalDate newDate = LocalDate.now();
        updatedUserAppointment.setDate(newDate);
        UserDto someUser = createUser();
        System.out.println(someUser);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(someUser))
                .when()
                .body(json)
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateUserAppointmentByIdAsAdmin() throws JsonProcessingException {
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
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateUserAppointmentByIdAsAnonymous() throws JsonProcessingException {
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
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void createUserAppointmentAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("date", equalTo(newUserAppointment.getDate().toString()));
    }

    @Test
    void createUserAppointmentAsAdmin() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void createUserAppointmentAsAnonymous() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void deleteUserAppointmentAsUser() throws JsonProcessingException {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        UserDto user = userAppointment.getUser();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void deleteUserAppointmentAsSomeUser() throws JsonProcessingException {
        UserDto someUser = createUser();
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(someUser))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteUserAppointmentAsAdmin() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteUserAppointmentAsAnonymous() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

}