package com.example.springweb.controllers.product;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

class CommonProductControllerTest extends BaseIntegrationTest {

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
                .get(CommonProductController.REQUEST_MAPPING)
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
                .get(CommonProductController.REQUEST_MAPPING)
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
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body(".", hasSize(productRepository.findAll().size()));
    }

    @Test
    void testGetProductByIdAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
    }

    @Test
    void testGetProductByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
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
                .get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId));
    }
}