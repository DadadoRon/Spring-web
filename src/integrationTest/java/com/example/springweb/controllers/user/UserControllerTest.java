package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends BaseIntegrationTest {
    private TestUserDto user;

    @BeforeEach
    void setUp() throws Exception {
        user = createUser();
    }

    @Test
    @SneakyThrows
    void testUpdateUserPasswordAsAdmin() {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomAlphabetic(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void testUpdateUserByIdAsUser() {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomNumeric(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @SneakyThrows
    void testUpdateUserByIdAsAnonymous() {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomAlphabetic(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }
}