package com.example.springweb.controllers.userappointment.byuser;

import com.example.springweb.ProductModels;
import com.example.springweb.UserAppointmentModels;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Product;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.repository.ProductRepository;
import com.example.springweb.repository.UserAppointmentRepository;
import com.example.springweb.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAppointmentByUserControllerTest {

    @LocalServerPort
    private Integer port;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final String USERAPPOINTMENT_ENDPOINT = "/api/v1/user/user-appointments";

    private final User admin = UserModels.createUser(Role.ADMIN);
    private final User user = UserModels.createUser(Role.USER);
    private final User anonymous = UserModels.createUser(null);
    private final Product product = ProductModels.createProduct();
    private final List<UserAppointment> userAppointmentList = UserAppointmentModels.createUserAppointmentList(user, product);

    private int getRandomIndex(int userAppointmentListSize) {
        Random random = new Random();
        return random.nextInt(userAppointmentListSize);
    }

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
    private UserRepository userRepository;

    @Autowired
    private UserAppointmentRepository userAppointmentRepository;

    @Autowired
    private ProductRepository productRepository;

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
        productRepository.save(product);
        userAppointmentRepository.saveAll(userAppointmentList);
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
    void getAllUserAppointmentsAsUser() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void getAllUserAppointmentsAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void getAllUserAppointmentsAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void updateUserAppointmentByIdAsUser() throws JsonProcessingException {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        System.out.println(userAppointmentList);
        Integer userAppointmentId = userAppointment.getId();
        System.out.println(userAppointmentId);
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
                .put(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(updatedUserAppointment.getId()))
                .body("date", equalTo(updatedUserAppointment.getDate().toString()));
    }

    @Test
    void updateUserAppointmentByIdAsSomeUser() throws JsonProcessingException {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        System.out.println(userAppointment);
        Integer userAppointmentId = userAppointment.getId();
        System.out.println(userAppointmentId);
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        LocalDate newDate = LocalDate.now();
        updatedUserAppointment.setDate(newDate);
        User someUser = UserModels.createUser(Role.USER);
        userRepository.save(someUser);
        System.out.println(someUser);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(someUser))
                .when()
                .body(json)
                .put(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateUserAppointmentByIdAsAdmin() throws JsonProcessingException {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        System.out.println(userAppointmentId);
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
                .put(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateUserAppointmentByIdAsAnonymous() throws JsonProcessingException {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
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
                .put(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void createUserAppointmentAsUser() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .createUserAppointmentByUserDto(product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body("date", equalTo(newUserAppointment.getDate().toString()));
    }

    @Test
    void createUserAppointmentAsAdmin() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .createUserAppointmentByUserDto(product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void createUserAppointmentAsAnonymous() throws JsonProcessingException {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .createUserAppointmentByUserDto(product.getId());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(USERAPPOINTMENT_ENDPOINT)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void deleteUserAppointmentAsUser() {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", USERAPPOINTMENT_ENDPOINT, userAppointmentId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void deleteUserAppointmentAsSomeUser() {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        User someUser = UserModels.createUser(Role.USER);
        userRepository.save(someUser);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(someUser))
                .when()
                .delete(String.format("%s/%s", USERAPPOINTMENT_ENDPOINT, userAppointmentId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteUserAppointmentAsAdmin() {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", USERAPPOINTMENT_ENDPOINT, userAppointmentId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteUserAppointmentAsAnonymous() {
        UserAppointment userAppointment = userAppointmentList.get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%s", USERAPPOINTMENT_ENDPOINT, userAppointmentId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

}