package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
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
    @SneakyThrows
    void testProfileAsAdmin() {
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testProfileAsUser() {
        TestUserDto user = createUser();
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testProfileAsAnonymous() {
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous)))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString()))
                .andExpect(status().isNotFound());
        mockMvc.perform(get(String.format("%s/profile", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void testRegisterUserAsAdmin() {
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
    }

    @Test
    @SneakyThrows
    void testRegisterUserAsUser() {
        TestUserDto user = createUser();
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
    }

    @Test
    @SneakyThrows
    void testRegisterUserAsAnonymous() {
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
        UserCreateDto newUser1 = UserModels.getUserCreateDto(Role.USER);
        String json1 = objectMapper.writeValueAsString(newUser1);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString())
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
    @SneakyThrows
    void testCacheAfterRegister() {
        String usersBeforeRegisterResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersBeforeRegister = objectMapper.readValue(usersBeforeRegisterResponse,
                new TypeReference<List<User>>() { });
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/register", CommonUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk());
        String usersAfterRegisterResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersAfterRegister = objectMapper.readValue(usersAfterRegisterResponse,
                new TypeReference<List<User>>() { });
        assertNotEquals(usersBeforeRegister, usersAfterRegister);
    }

    @Test
    @SneakyThrows
    void testResetPasswordAsUser() {
        TestUserDto user = createUser();
        String email = user.email();
        UserResetPasswordDto userResetPasswordDto = new UserResetPasswordDto(email);
        String json = objectMapper.writeValueAsString(userResetPasswordDto);
        System.out.println(json);
        mockMvc.perform(post(String.format("%s/password/reset", CommonUserController.REQUEST_MAPPING))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user))
                        .content(json))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testResetPasswordAsAnonymous() {
        String email = anonymous.getEmail();
        UserResetPasswordDto userResetPasswordDto = new UserResetPasswordDto(email);
        String json = objectMapper.writeValueAsString(userResetPasswordDto);
        System.out.println(json);
        mockMvc.perform(post(String.format("%s/password/reset", CommonUserController.REQUEST_MAPPING))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous))
                        .content(json))
                .andExpect(status().isNotFound());
    }
}