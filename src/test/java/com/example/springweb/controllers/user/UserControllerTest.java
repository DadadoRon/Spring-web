package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testUpdateUserPasswordAsAdmin() throws Exception {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomAlphabetic(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUserByIdAsUser() throws Exception {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomNumeric(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateUserByIdAsAnonymous() throws Exception {
        String oldPassword = user.password();
        String newPassword = (RandomStringUtils.randomAlphabetic(8) + "@@");
        PasswordUpdateDtoByUser password = new PasswordUpdateDtoByUser(oldPassword, newPassword);
        String json = objectMapper.writeValueAsString(password);
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }
}