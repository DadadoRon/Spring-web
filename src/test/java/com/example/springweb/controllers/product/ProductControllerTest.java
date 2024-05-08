package com.example.springweb.controllers.product;

import com.example.springweb.ProductModels;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Product;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.repository.ProductRepository;
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
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private Integer port;

    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    private static final String PRODUCTS_ENDPOINT = "/api/v1/products";

    private final User user = UserModels.createUser(Role.USER);
    private final User admin = UserModels.createUser(Role.ADMIN);
    private final User anonymous = UserModels.createUser(null);
    private final List<Product> productList = ProductModels.createRandomProductList();

    private int getRandomIndex(int productListSize) {
        Random random = new Random();
        return random.nextInt(productListSize);
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
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    void setUp() {
        productRepository.saveAll(productList);
        userRepository.save(admin);
        userRepository.save(user);
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @AfterEach
    void delete() {
        userRepository.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void getAllProductsAsAdmin() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void getAllUsersAsUser() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void getAllUsersAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void getProductByIdAsAdmin() {
        Product product = productList.get(getRandomIndex(productList.size()));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/%s", PRODUCTS_ENDPOINT, product.getId()))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(product.getId()));
    }

    @Test
    void getProductByIdAsUser() {
        System.out.println(productList);
        Product product = productList.get(getRandomIndex(productList.size()));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/%s", PRODUCTS_ENDPOINT, product.getId()))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(product.getId()));
    }

    @Test
    void getProductByIdAsAnonymous() {
        Product product = productList.get(getRandomIndex(productList.size()));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(String.format("%s/%s", PRODUCTS_ENDPOINT, product.getId()))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(product.getId()));
    }

    @Test
    void updateProductByIdAsAdmin() throws JsonProcessingException {
        Product product = productList.get(getRandomIndex(productList.size()));
        Integer productId = product.getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8,12);
        updatedProduct.setName(newName);
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .put(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId))
                .body("name", equalTo(newName));
    }

    @Test
    void updateProductByIdAsUser() throws JsonProcessingException {
        Product product = productList.get(getRandomIndex(productList.size()));
        Integer productId = product.getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        updatedProduct.setName(RandomStringUtils.randomAlphabetic(8,12));
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .put(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateProductByIdAsAnonymous() throws JsonProcessingException {
        Product product = productList.get(getRandomIndex(productList.size()));
        Integer productId = product.getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        updatedProduct.setName(RandomStringUtils.randomAlphabetic(8,12));
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .put(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void createProductAsAdmin() throws JsonProcessingException {
        ProductCreateDto newProductDto = ProductModels.createProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(newProductDto.getName()));
    }

    @Test
    void createProductAsUser() throws JsonProcessingException {
        ProductCreateDto newProductDto = ProductModels.createProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void createProductAsAnonymous() throws JsonProcessingException {
        ProductCreateDto newProductDto = ProductModels.createProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(PRODUCTS_ENDPOINT)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void deleteProductAsAdmin() {
        Product product = productList.get(getRandomIndex(productList.size()));
        Integer productId = product.getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", PRODUCTS_ENDPOINT, productId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void deleteProductAsUser() {
        Product product = productList.get(getRandomIndex(productList.size()));
        Integer productId = product.getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", PRODUCTS_ENDPOINT, productId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteProductAsAnonymous() {
        Product product = productList.get(getRandomIndex(productList.size()));
        Integer productId = product.getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%s", PRODUCTS_ENDPOINT, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}