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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AdminProductControllerTest extends BaseIntegrationTest {

    private static List<ProductDto> productList = new ArrayList<>();

    @BeforeEach
    void setUp() throws JsonProcessingException {
        RestAssured.baseURI = "http://localhost:" + port;
        productList = createProducts();

    }


    @Test
    void testUpdateProductByIdAsAdmin() throws JsonProcessingException {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8, 12);
        updatedProduct.setName(newName);
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .put(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId))
                .body("name", equalTo(newName));
    }

    @Test
    void testUpdateProductByIdAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        updatedProduct.setName(RandomStringUtils.randomAlphabetic(8, 12));
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .body(json)
                .put(AdminProductController.REQUEST_MAPPING)
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
        updatedProduct.setName(RandomStringUtils.randomAlphabetic(8, 12));
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .body(json)
                .put(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .put(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .put(AdminProductController.REQUEST_MAPPING)
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
                .post(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(newProductDto.getName()));
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
                .post(AdminProductController.REQUEST_MAPPING)
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
                .post(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .body(json)
                .post(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .body(json)
                .post(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testDeleteProductAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK);
    }

    @Test
    void testDeleteProductAsUser() throws JsonProcessingException {
        UserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(user))
                .when()
                .delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_FORBIDDEN);
    }

    @Test
    void testDeleteProductAsAnonymous() {
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(anonymous))
                .when()
                .delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .header(randomString)
                .when()
                .delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    void testCacheAfterUpdate() throws JsonProcessingException {
        List<Product> productsBeforeUpdate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Product.class);
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8, 12);
        updatedProduct.setName(newName);
        String json = objectMapper.writeValueAsString(updatedProduct);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .put(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("id", equalTo(productId))
                .body("name", equalTo(newName));
        List<Product> productsAfterUpdate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Product.class);
        assertNotEquals(productsBeforeUpdate, productsAfterUpdate);
    }

    @Test
    void testCacheAfterCreate() throws JsonProcessingException {
        List<Product> productsBeforeCreate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Product.class);
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .body(json)
                .post(AdminProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .body("name", equalTo(newProductDto.getName()));
        List<Product> productsAfterCreate = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Product.class);
        assertNotEquals(productsBeforeCreate, productsAfterCreate);
    }

    @Test
    void testCacheAfterDelete() throws JsonProcessingException {
        List<Product> productsBeforeDelete = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Product.class);
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        assertTrue(productRepository.existsById(productId));
        given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .then()
                .statusCode(SC_OK);
        List<Product> productsAfterDelete = given()
                .contentType(ContentType.JSON)
                .header(getAuthorizationHeader(admin))
                .when()
                .get(CommonProductController.REQUEST_MAPPING)
                .then()
                .statusCode(SC_OK)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Product.class);
        assertNotEquals(productsBeforeDelete, productsAfterDelete);
    }
}