package com.example.springweb.controllers.user;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserModels;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminUserControllerTest extends BaseIntegrationTest {

    private static List<UserDto> userList = new ArrayList<>();

    @BeforeEach
    void setUp() throws Exception {
        userList = createUsers();
    }

    @Test
    void testGetUserByIdAsAdmin() throws Exception {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId));
    }

    @Test
    void testGetUserByIdAsUser() throws Exception {
        TestUserDto user = createUser();
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetUserByIdAsAnonymous() throws Exception {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        mockMvc.perform(get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetAllUsersAsAdmin() throws Exception {
        mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userRepository.findAll().size())));
    }

    @Test
    void testGetAllUsersAsUser() throws Exception {
        TestUserDto user = createUser();
        mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllUsersAsAnonymous() throws Exception {
        mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUserByIdAsAdmin() throws Exception {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        String newFirstName = RandomStringUtils.randomAlphabetic(8,12);
        updatedUser.setFirstName(newFirstName);
        String json = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value(newFirstName));
    }

    @Test
    void testUpdateUserByIdAsUser() throws Exception {
        TestUserDto user = createUser();
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        updatedUser.setFirstName(RandomStringUtils.randomAlphabetic(8,12));
        String json = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue())
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUserByIdAsAnonymous() throws Exception {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        updatedUser.setFirstName(RandomStringUtils.randomAlphabetic(8,12));
        String json = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateUserAsAdmin() throws Exception {
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
    }

    @Test
    void testCreateUserAsUser() throws Exception {
        TestUserDto user = createUser();
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue())
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateUserAsAnonymous() throws Exception {
        User newUser = UserModels.createUser(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void testDeleteUserAsAdmin() throws Exception {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        assertTrue(userRepository.existsById(userId));
        mockMvc.perform(delete(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserAsUser() throws Exception {
        TestUserDto user = createUser();
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        assertTrue(userRepository.existsById(userId));
        userRepository.deleteById(userId);
        mockMvc.perform(delete(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUserAsAnonymous() throws Exception {
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        userRepository.deleteById(userId);
        mockMvc.perform(delete(String.format("%s/%d", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(String.format("%s/%d", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(String.format("%s/%d", AdminUserController.REQUEST_MAPPING, userId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCacheAfterUpdate() throws Exception {
        String usersBeforeUpdateResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersBeforeUpdate = objectMapper.readValue(usersBeforeUpdateResponse, new TypeReference<List<User>>() {});
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        Optional<User> byId = userRepository.findById(userId);
        assertTrue(byId.isPresent());
        User updatedUser = byId.get();
        String newFirstName = RandomStringUtils.randomAlphabetic(8,12);
        updatedUser.setFirstName(newFirstName);
        String json = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(put(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.firstName").value(newFirstName));
        String userAfterUpdateResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersAfterUpdate = objectMapper.readValue(userAfterUpdateResponse, new TypeReference<List<User>>() {});
        assertNotEquals(usersBeforeUpdate, usersAfterUpdate);
    }

    @Test
    void testCacheAfterCreate() throws Exception {
        String usersBeforeCreateResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersBeforeCreate = objectMapper.readValue(usersBeforeCreateResponse, new TypeReference<List<User>>(){});
        UserCreateDto newUser = UserModels.getUserCreateDto(Role.USER);
        String json = objectMapper.writeValueAsString(newUser);
        mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(newUser.firstName()));
        String usersAfterCreateResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(),getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersAfterCreate = objectMapper.readValue(usersAfterCreateResponse, new TypeReference<List<User>>() {});
        assertNotEquals(usersBeforeCreate, usersAfterCreate);
    }

    @Test
    void testCacheAfterDelete() throws Exception {
        String usersBeforeDeleteResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersBeforeDelete = objectMapper.readValue(usersBeforeDeleteResponse, new TypeReference<List<User>>() {});
        Integer userId = userList.get(getRandomIndex(userList.size())).id();
        assertTrue(userRepository.existsById(userId));
        mockMvc.perform(delete(String.format("%s/%s", AdminUserController.REQUEST_MAPPING, userId))
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk());
        String usersAfterDeleteResponse = mockMvc.perform(get(AdminUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<User> usersAfterDelete = objectMapper.readValue(usersAfterDeleteResponse, new TypeReference<List<User>>() {});
        assertNotEquals(usersBeforeDelete, usersAfterDelete);
    }
}

