package com.example.springweb.controllers.userappointment.byuser;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserAppointmentModels;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class UserAppointmentByUserControllerTest extends BaseIntegrationTest {

    private List<UserAppointmentDto> userAppointmentList = new ArrayList<>();

    private ProductDto product;

    private TestUserDto userDto;


    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        product = createProduct();
        userDto = createUser();
        userAppointmentList = createUserAppointments(product.id(), userDto);
    }

    @Test
    void testGetAllUserAppointmentsAsUser() throws JsonProcessingException {
        TestUserDto user = createUser();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testGetAllUserAppointmentsAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testGetAllUserAppointmentsAsAnonymous() {
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
    void testUpdateUserAppointmentByIdAsUser() throws JsonProcessingException {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .body(json)
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(updatedUserAppointment.getId()))
                .body("dateTime", equalTo(updatedUserAppointment.getDateTime()
                        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
    }

    @Test
    void testUpdateUserAppointmentByIdAsSomeUser() throws JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        TestUserDto someUser = createUser();
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
    void testUpdateUserAppointmentByIdAsAdmin() throws JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
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
    void testUpdateUserAppointmentByIdAsAnonymous() throws JsonProcessingException {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
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
    void testCreateUserAppointmentAsUser() throws JsonProcessingException {
        TestUserDto user = createUser();
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK);

    }

    @Test
    void testCreateUserAppointmentAsAdmin() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
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
    void testCreateUserAppointmentAsAnonymous() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
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
    void testDeleteUserAppointmentAsUser() throws JsonProcessingException {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testDeleteUserAppointmentAsSomeUser() throws JsonProcessingException {
        TestUserDto someUser = createUser();
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
    void testDeleteUserAppointmentAsAdmin() {
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
    void testDeleteUserAppointmentAsAnonymous() {
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

    @Test
    void testCacheAfterUpdate() throws JsonProcessingException {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        List<UserAppointment> userAppointmentsBeforeUpdate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserAppointment.class);
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .body(json)
                .put(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(updatedUserAppointment.getId()))
                .body("dateTime", equalTo(updatedUserAppointment.getDateTime()
                        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)));
        List<UserAppointment> userAppointmentsAfterUpdate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserAppointment.class);
        assertNotEquals(userAppointmentsBeforeUpdate, userAppointmentsAfterUpdate);
    }

    @Test
    void testCacheAfterCreate() throws JsonProcessingException {
        TestUserDto user = createUser();
        List<UserAppointment> userAppointmentsBeforeCreate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserAppointment.class);
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK);
        List<UserAppointment> userAppointmentsAfterCreate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserAppointment.class);
        assertNotEquals(userAppointmentsBeforeCreate, userAppointmentsAfterCreate);
    }

    @Test
    void testCacheAfterDelete() {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        List<UserAppointment> userAppointmentsBeforeDelete = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserAppointment.class);
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .then()
                .statusCode(SC_OK);
        List<UserAppointment> userAppointmentsAfterDelete = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(userDto))
                .when()
                .get(UserAppointmentByUserController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", UserAppointment.class);
        assertNotEquals(userAppointmentsBeforeDelete, userAppointmentsAfterDelete);
    }
}