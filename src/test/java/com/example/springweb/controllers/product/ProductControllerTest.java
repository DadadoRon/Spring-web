package com.example.springweb.controllers.product;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.ProductModels;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import static org.junit.jupiter.api.Assertions.assertTrue;


class ProductControllerTest extends BaseIntegrationTest {

    private static List<ProductDto> productList = new ArrayList<>();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        userRepository.save(admin);
        productList = createProductList();
//        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void testGetAllProductsAsAdmin() {
        System.out.println(productList);
        System.out.println(userRepository.findAll());
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void getAllProductsAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void getAllProductsAsAnonymous() {
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void getProductByIdAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
    }

    @Test
    void getProductByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
    }

    @Test
    void getProductByIdAsAnonymous() {
        List<Product> productList = productRepository.findAll();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .get(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
    }

    @Test
    void updateProductByIdAsAdmin() throws JsonProcessingException {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
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
                .put(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId))
                .body("name", equalTo(newName));
    }

    @Test
    void updateProductByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
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
                .put(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void updateProductByIdAsAnonymous() throws JsonProcessingException {
        List<Product> productList = productRepository.findAll();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
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
                .put(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .put(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .put(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void createProductAsAdmin() throws JsonProcessingException {
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(newProductDto.getName()));
    }

    @Test
    void createProductAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .post(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void createProductAsAnonymous() throws JsonProcessingException {
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .post(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .post(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .post(ProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void deleteProductAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void deleteProductAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void deleteProductAsAnonymous() {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .delete(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format("%s/%s", ProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }
}