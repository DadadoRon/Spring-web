package com.example.springweb.controllers.product;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.ProductModels;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.entity.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminProductControllerTest extends BaseIntegrationTest {

    private static List<ProductDto> productList = new ArrayList<>();


    @BeforeEach
    void setUp() throws Exception {
        productList = createProducts();

    }

    @Test
    void testUpdateProductByIdAsAdmin() throws Exception {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8, 12);
        updatedProduct.setName(newName);
        String json = objectMapper.writeValueAsString(updatedProduct);
        mockMvc.perform(put(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(newName));
    }

    @Test
    void testUpdateProductByIdAsUser() throws Exception {
        TestUserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        updatedProduct.setName(RandomStringUtils.randomAlphabetic(8, 12));
        String json = objectMapper.writeValueAsString(updatedProduct);
        mockMvc.perform(put(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateProductByIdAsAnonymous() throws Exception {
        List<Product> productList = productRepository.findAll();
        Integer productId = productList.get(getRandomIndex(productList.size())).getId();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        updatedProduct.setName(RandomStringUtils.randomAlphabetic(8, 12));
        String json = objectMapper.writeValueAsString(updatedProduct);
        mockMvc.perform(put(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateProductAsAdmin() throws Exception {
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newProductDto.name()));

    }

    @Test
    void testCreateProductAsUser() throws Exception {
        TestUserDto user = createUser();
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateProductAsAnonymous() throws Exception {
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteProductAsAdmin() throws Exception {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        assertTrue(productRepository.existsById(productId));
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteProductAsUser() throws Exception {
        TestUserDto user = createUser();
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        assertTrue(productRepository.existsById(productId));
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteProductAsAnonymous() throws Exception {
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        assertTrue(productRepository.existsById(productId));
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous)))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(MockMvcRequestBuilders.delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCacheAfterUpdate() throws Exception {
        String productsBeforeUpdateResponse = mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Product> productsBeforeUpdate = objectMapper.readValue(productsBeforeUpdateResponse, new TypeReference<List<Product>>(){});
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        Optional<Product> byId = productRepository.findById(productId);
        assertTrue(byId.isPresent());
        Product updatedProduct = byId.get();
        String newName = RandomStringUtils.randomAlphabetic(8, 12);
        updatedProduct.setName(newName);
        String json = objectMapper.writeValueAsString(updatedProduct);
        mockMvc.perform(put(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.name").value(newName));
        String productsAfterUpdateResponse = mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Product> productsAfterUpdate = objectMapper.readValue(productsAfterUpdateResponse, new TypeReference<List<Product>>(){});
        assertNotEquals(productsBeforeUpdate, productsAfterUpdate);
    }

    @Test
    void testCacheAfterCreate() throws Exception {
        String productsBeforeCreateResponse = mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Product> productsBeforeCreate = objectMapper.readValue(productsBeforeCreateResponse, new TypeReference<List<Product>>(){});
        ProductCreateDto newProductDto = ProductModels.getProductDto();
        String json = objectMapper.writeValueAsString(newProductDto);
        mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(newProductDto.name()));
        String productsAfterCreateResponse = mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Product> productsAfterCreate = objectMapper.readValue(productsAfterCreateResponse, new TypeReference<List<Product>>(){});
        assertNotEquals(productsBeforeCreate, productsAfterCreate);
    }

    @Test
    void testCacheAfterDelete() throws Exception {
        String productsBeforeDeleteResponse = mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Product> productsBeforeDelete = objectMapper.readValue(productsBeforeDeleteResponse, new TypeReference<List<Product>>(){});
        Integer productId = productList.get(getRandomIndex(productList.size())).id();
        assertTrue(productRepository.existsById(productId));
        mockMvc.perform(delete(String.format("%s/%s", AdminProductController.REQUEST_MAPPING, productId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
        String productsAfterDeleteResponse = mockMvc.perform(get(CommonProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<Product> productsAfterDelete = objectMapper.readValue(productsAfterDeleteResponse, new TypeReference<List<Product>>(){});
        assertNotEquals(productsBeforeDelete, productsAfterDelete);
    }
}