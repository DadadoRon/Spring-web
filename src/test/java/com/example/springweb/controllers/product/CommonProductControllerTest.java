package com.example.springweb.controllers.product;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.entity.Product;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonProductControllerTest extends BaseIntegrationTest {

    private static List<ProductDto> productList = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        productList = createProducts();
    }

    @Test
    @SneakyThrows
    void testGetAllProductsAsAdmin() {
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
    }

    @Test
    @SneakyThrows
    void testGetAllProductsAsUser() {
        TestUserDto user = createUser();
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
    }

    @Test
    @SneakyThrows
    void testGetAllProductsAsAnonymous() {
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
    }

    @Test
    @SneakyThrows
    void testGetProductByIdAsAdmin() {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    @SneakyThrows
    void testGetProductByIdAsUser() {
        TestUserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    @SneakyThrows
    void testGetProductByIdAsAnonymous() {
        List<Product> productList = productRepository.findAll();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
    }
}