package com.example.springweb.controllers.product;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.ProductModels;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.entity.Product;
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
import static org.junit.jupiter.api.Assertions.assertTrue;


class ProductControllerTest extends BaseIntegrationTest {

    private static List<ProductDto> productList = new ArrayList<>();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        productList = createProducts();

    }

    @Test
    void testGetAllProductsAsAdmin() {
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
    void testGetAllProductsAsUser() throws JsonProcessingException {
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
    void testGetAllProductsAsAnonymous() {
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
    void testGetProductByIdAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
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
    void testGetProductByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
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
    void testGetProductByIdAsAnonymous() {
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
    void testUpdateProductByIdAsAdmin() throws JsonProcessingException {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
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
    void testUpdateProductByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8,12);
        updatedProduct.setName(newName);
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
    void testUpdateProductByIdAsAnonymous() throws JsonProcessingException {
        List<Product> productList = productRepository.findAll();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8,12);
        updatedProduct.setName(newName);
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
    void testCreateProductAsAdmin() throws JsonProcessingException {
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
                .body("name", equalTo(newProductDto.name()));
    }

    @Test
    void testCreateProductAsUser() throws JsonProcessingException {
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
    void testCreateProductAsAnonymous() throws JsonProcessingException {
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
    void testDeleteProductAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
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
    void testDeleteProductAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
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
    void testDeleteProductAsAnonymous() {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
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