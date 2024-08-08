package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommonUserControllerTest extends BaseIntegrationTest {
    @Test
    void testProfileAsAdmin() throws Exception {
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
    }

    @Test
    void testProfileAsUser() throws Exception {
        TestUserDto user = createUser();
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user)))
                .andExpect(status().isOk());
    }

    @Test
    void testProfileAsAnonymous() throws Exception {
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous)))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testRegisterUserAsAdmin() throws Exception {
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
    }

    @Test
    void testRegisterUserAsUser() throws Exception {
        TestUserDto user = createUser();
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
    }

    @Test
    void testRegisterUserAsAnonymous() throws Exception {
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
        UserCreateDto newUser1 = UserModels.getUserCreateDto(Role.USER);
        String json1 = objectMapper.writeValueAsString(newUser1);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,randomString())
                .content(json1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser1.firstName()));
        UserCreateDto newUser2 = UserModels.getUserCreateDto(Role.USER);
        String json2 = objectMapper.writeValueAsString(newUser2);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser2.firstName()));
    }

    @Test
    void testCacheAfterRegister() throws Exception {
        String usersBeforeRegisterResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersBeforeRegister = objectMapper.readValue(usersBeforeRegisterResponse, new TypeReference<List<User>>() {});
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk());
        String usersAfterRegisterResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION,getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersAfterRegister = objectMapper.readValue(usersAfterRegisterResponse, new TypeReference<List<User>>() {});
        assertNotEquals(usersBeforeRegister, usersAfterRegister);
    }
}