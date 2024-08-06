package com.example.springweb.controllers.product;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testGetAllProductsAsAdmin() throws Exception {
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
    }

    @Test
    void testGetAllProductsAsUser() throws Exception {
        TestUserDto user = createUser();
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
    }

    @Test
    void testGetAllProductsAsAnonymous() throws Exception {
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
        mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(productRepository.findAll().size())));
    }

    @Test
    void testGetProductByIdAsAdmin() throws Exception {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    void testGetProductByIdAsUser() throws Exception {
        TestUserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
    }

    @Test
    void testGetProductByIdAsAnonymous() throws Exception {
        List<Product> productList = productRepository.findAll();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
        mockMvc.perform(get(String.format("%s/%s", CommonProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId));
    }
}