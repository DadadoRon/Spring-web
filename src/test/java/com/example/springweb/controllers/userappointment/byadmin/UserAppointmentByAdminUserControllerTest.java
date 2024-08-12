package com.example.springweb.controllers.userappointment.byadmin;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserAppointmentModels;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserAppointmentByAdminUserControllerTest extends BaseIntegrationTest {

    private List<UserAppointmentDto> userAppointmentList = new ArrayList<>();

    private TestUserDto user;

    private ProductDto product;

    @BeforeEach
    void setUp() throws Exception {
        user = createUser();
        product = createProduct();
        userAppointmentList = createUserAppointments(user.id(), product.id());
    }

    @Test
    @SneakyThrows
    void testGetAllUserAppointmentsAsAdmin() {
        mockMvc.perform(get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(userAppointmentRepository.findAll().size())));
    }

    @Test
    @SneakyThrows
    void testGetAllUserAppointmentsAsUser() {
        TestUserDto user = createUser();
        mockMvc.perform(get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void testGetAllUserAppointmentsAsAnonymous() {
        mockMvc.perform(get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous)))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testUpdateUserAppointmentByIdAsAdmin() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserAppointment.getId()))
                .andExpect(jsonPath("$.dateTime")
                        .value(updatedUserAppointment.getDateTime().truncatedTo(ChronoUnit.SECONDS)
                .format(DateTimeFormatter.ISO_INSTANT)));
    }

    @Test
    @SneakyThrows
    void testUpdateUserAppointmentByIdAsUser() {
        TestUserDto user = createUser();
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void testUpdateUserAppointmentByIdAsAnonymous() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testCreateUserAppointmentAsAdmin() {
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.id(), product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime").value(newUserAppointment.dateTime().truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
    }

    @Test
    @SneakyThrows
    void testCreateUserAppointmentAsUser() {
        TestUserDto user = createUser();
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.id(), product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user))
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void testCreateUserAppointmentAsAnonymous() {
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.id(), product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous))
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testDeleteUserAppointmentAsAdmin() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testDeleteUserAppointmentAsUser() {
        TestUserDto user = createUser();
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void testDeleteUserAppointmentAsAnonymous() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous)))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testCheckIfExistsUserAppointmentByAdmin() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
    }

    @Test
    @SneakyThrows
    void testCheckIfExistsUserAppointmentByUser() {
        TestUserDto user = createUser();
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user)))
                .andExpect(status().isForbidden());
    }

    @Test
    @SneakyThrows
    void testCheckIfExistsUserAppointmentByAnonymous() {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(anonymous)))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, randomString()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @SneakyThrows
    void testCacheAfterUpdate() {
        String userAppointmentsBeforeUpdateResponse = mockMvc.perform(get(
                UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsBeforeUpdate = objectMapper
                .readValue(userAppointmentsBeforeUpdateResponse, new TypeReference<List<UserAppointment>>() { });
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserAppointment.getId()))
                .andExpect(jsonPath("$.dateTime").value(updatedUserAppointment.getDateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
        String userAppointmentsAfterUpdateResponse = mockMvc.perform(get(
                UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsAfterUpdate = objectMapper.readValue(userAppointmentsAfterUpdateResponse,
                new TypeReference<List<UserAppointment>>() { });
        assertNotEquals(userAppointmentsBeforeUpdate, userAppointmentsAfterUpdate);
    }

    @Test
    @SneakyThrows
    void testCacheAfterCreate() {
        String userAppointmentsBeforeCreateResponse = mockMvc.perform(get(
                UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsBeforeCreate = objectMapper.
                readValue(userAppointmentsBeforeCreateResponse,
                new TypeReference<List<UserAppointment>>() { });
        UserAppointmentByAdminCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByAdminDto(user.id(), product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime").value(newUserAppointment.dateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
        String userAppointmentsAfterCreateResponse = mockMvc.perform(get(
                UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsAfterCreate = objectMapper.readValue(userAppointmentsAfterCreateResponse,
                new TypeReference<List<UserAppointment>>() { });
        assertNotEquals(userAppointmentsBeforeCreate, userAppointmentsAfterCreate);
    }

    @Test
    @SneakyThrows
    void testCacheAfterDelete() {
        String userAppointmentsBeforeDeleteResponse = mockMvc.perform(get(
                UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsBeforeDelete = objectMapper.
                readValue(userAppointmentsBeforeDeleteResponse,
                new TypeReference<List<UserAppointment>>() { });
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(
                String.format("%s/%s", UserAppointmentByAdminController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk());
        String userAppointmentsAfterDeleteResponse = mockMvc.perform(get(
                UserAppointmentByAdminController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsAfterDelete = objectMapper.readValue(userAppointmentsAfterDeleteResponse,
                new TypeReference<List<UserAppointment>>() { });
        assertNotEquals(userAppointmentsBeforeDelete, userAppointmentsAfterDelete);
    }
}
