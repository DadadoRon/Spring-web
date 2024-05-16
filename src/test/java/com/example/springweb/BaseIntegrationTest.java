package com.example.springweb;

import com.example.springweb.controllers.product.ProductController;
import com.example.springweb.controllers.product.ProductCreateDto;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.UserController;
import com.example.springweb.controllers.user.UserCreateDto;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminController;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserController;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserCreateDto;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.repository.ProductRepository;
import com.example.springweb.repository.UserAppointmentRepository;
import com.example.springweb.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static com.example.springweb.PostgreSQLContainerExtension.postgres;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;


@SpringBootTest
@ExtendWith(PostgreSQLContainerExtension.class)
public abstract class BaseIntegrationTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public UserAppointmentRepository userAppointmentRepository;

    @Autowired
    public  ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void delete() {
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();

    }

    public static Header getAuthorizationHeader(User user) {
        String authorizationHeaderValue = String.format("Basic %s", Base64.getEncoder().encodeToString(
                String.format("%s:%s", user.getEmail(), user.getPassword()).getBytes()));
        return new Header("Authorization", authorizationHeaderValue);
    }

    public static Header getAuthorizationHeader(UserDto user) {
        String authorizationHeaderValue = String.format("Basic %s", Base64.getEncoder().encodeToString(
                String.format("%s:%s", user.getEmail(), user.getPassword()).getBytes()));
        return new Header("Authorization", authorizationHeaderValue);
    }

    public static int getRandomIndex(int listSize) {
        Random random = new Random();
        return random.nextInt(listSize);
    }

    public final User anonymous = UserModels.createUser(null);

    public final Header randomString = new Header("Authorization", RandomStringUtils.randomAlphabetic(10));

    public static final User admin = UserModels.createUser(Role.ADMIN);



    public ProductDto createProduct() throws JsonProcessingException {
        ProductCreateDto productCreateDto = ProductModels.getProductDto();
        String jsonProduct = objectMapper.writeValueAsString(productCreateDto);
        return given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(jsonProduct)
                .post(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract().body().as(ProductDto.class);
    }


    public List<ProductDto> createProductList() throws JsonProcessingException {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductCreateDto> productCreateDtoList = ProductModels.getRandomProductDtoList();
        for (ProductCreateDto productCreateDto : productCreateDtoList) {
            String json = objectMapper.writeValueAsString(productCreateDto);
            Header authorizationHeader = getAuthorizationHeader(admin);
            System.out.println(authorizationHeader);
            ProductDto productDto = given()
                    .contentType(ContentType.JSON)
                    .header(getAuthorizationHeader(admin))
                    .when()
                    .body(json)
                    .post(ProductController.REQUEST_MAPPING)
                    .then()
                    .statusCode(SC_OK)
                    .extract().body().as(ProductDto.class);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public UserDto createUser() throws JsonProcessingException {
        UserCreateDto userCreateDto = UserModels.getUserCreateDto(Role.USER);
        String jsonUser = objectMapper.writeValueAsString(userCreateDto);
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization",String.format("Basic %s", "cGlua0BtYWlsLmNvbTozMDEzMDEzMDFA"))
                .when()
                .body(jsonUser)
                .post(String.format("%s/create", UserController.REQUEST_MAPPING))
                .then()
                .statusCode(SC_OK)
                .extract().body().as(UserDto.class);

    }

    public List<UserDto> createUserList() throws JsonProcessingException {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserCreateDto> userCreateDtoList = UserModels.getRandomUserCreateDtoList();
        for (UserCreateDto userCreateDto : userCreateDtoList) {
            String json = objectMapper.writeValueAsString(userCreateDto);
            UserDto userDto = given()
                    .contentType(ContentType.JSON)
                    .header(getAuthorizationHeader(admin))
                    .when()
                    .body(json)
                    .post(String.format("%s/create", UserController.REQUEST_MAPPING))
                    .then()
                    .statusCode(SC_OK)
                    .extract().body().as(UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public List<UserAppointmentDto> createUserAppointmentList(Integer userId, Integer productId) throws JsonProcessingException {
        List<UserAppointmentDto> userAppointmentList = new ArrayList<>();
        List<UserAppointmentByAdminCreateDto> userAppointmentByAdminCreateDtos = UserAppointmentModels.getRandomUserAppointmentCreateDto(userId, productId);
        for (UserAppointmentByAdminCreateDto userAppointmentByAdminCreateDto : userAppointmentByAdminCreateDtos) {
            String json = objectMapper.writeValueAsString(userAppointmentByAdminCreateDto);
            UserAppointmentDto userAppointmentDto = given()
                    .contentType(ContentType.JSON)
                    .header(getAuthorizationHeader(admin))
                    .when()
                    .body(json)
                    .post(UserAppointmentByAdminController.REQUEST_MAPPING)
                    .then()
                    .statusCode(SC_OK)
                    .extract().body().as(UserAppointmentDto.class);
            userAppointmentList.add(userAppointmentDto);
        }
        return userAppointmentList;
    }

    public List<UserAppointmentDto> createUserAppointmentList(Integer productId) throws JsonProcessingException {
        UserDto user = createUser();
        List<UserAppointmentDto> userAppointmentList = new ArrayList<>();
        List<UserAppointmentByUserCreateDto> userAppointmentByUserCreateDtos = UserAppointmentModels.getRandomUserAppointmentCreateDtoByUser(productId);
        for (UserAppointmentByUserCreateDto userAppointmentByUserCreateDto : userAppointmentByUserCreateDtos) {
            String json = objectMapper.writeValueAsString(userAppointmentByUserCreateDto);
            UserAppointmentDto userAppointmentDto = given()
                    .contentType(ContentType.JSON)
                    .header(getAuthorizationHeader(user))
                    .when()
                    .body(json)
                    .post(UserAppointmentByUserController.REQUEST_MAPPING)
                    .then()
                    .statusCode(SC_OK)
                    .extract().body().as(UserAppointmentDto.class);
            userAppointmentList.add(userAppointmentDto);
        }
        return  userAppointmentList;
    }
}
