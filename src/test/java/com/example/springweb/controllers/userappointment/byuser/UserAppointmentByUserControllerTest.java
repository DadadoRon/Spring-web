package com.example.springweb.controllers.userappointment.byuser;

import com.example.springweb.BaseIntegrationTest;
import com.example.springweb.UserAppointmentModels;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.entity.UserAppointment;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserAppointmentByUserControllerTest extends BaseIntegrationTest {

    private List<UserAppointmentDto> userAppointmentList = new ArrayList<>();

    private ProductDto product;

    private TestUserDto userDto;


    @BeforeEach
    void setUp() throws Exception {
        product = createProduct();
        userDto = createUser();
        userAppointmentList = createUserAppointments(userDto.id(), product.id());
    }

    @Test
    void testGetAllUserAppointmentsAsUser() throws Exception {
        TestUserDto user = createUser();
        mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(),getAuthorizationHeader(user).getValue()))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllUserAppointmentsAsAdmin() throws Exception {
        mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testGetAllUserAppointmentsAsAnonymous() throws Exception {
        mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(),getAuthorizationHeader(anonymous).getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(),randomString.getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateUserAppointmentByIdAsUser() throws Exception {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserAppointment.getId()))
                .andExpect(jsonPath("$.dateTime").value(updatedUserAppointment.getDateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
    }

    @Test
    void testUpdateUserAppointmentByIdAsSomeUser() throws Exception {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        TestUserDto someUser = createUser();
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(someUser).getName(), getAuthorizationHeader(someUser).getValue())
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUserAppointmentByIdAsAdmin() throws Exception {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(), getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testUpdateUserAppointmentByIdAsAnonymous() throws Exception {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(), getAuthorizationHeader(anonymous).getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(), randomString.getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCreateUserAppointmentAsUser() throws Exception {
        TestUserDto user = createUser();
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(),getAuthorizationHeader(user).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime").value(newUserAppointment.dateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
    }

    @Test
    void testCreateUserAppointmentAsAdmin() throws Exception {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(),getAuthorizationHeader(admin).getValue())
                .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateUserAppointmentAsAnonymous() throws Exception {
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(),getAuthorizationHeader(anonymous).getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(),randomString.getValue())
                .content(json))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testDeleteUserAppointmentAsUser() throws Exception {
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(),getAuthorizationHeader(userDto).getValue()))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUserAppointmentAsSomeUser() throws Exception {
        TestUserDto someUser = createUser();
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(someUser).getName(),getAuthorizationHeader(someUser).getValue()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUserAppointmentAsAdmin() throws Exception {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(admin).getName(),getAuthorizationHeader(admin).getValue()))
                .andExpect(status().isForbidden());
    }

    @Test
    void testDeleteUserAppointmentAsAnonymous() throws Exception {
        Integer userAppointmentId = userAppointmentList.get(getRandomIndex(userAppointmentList.size())).getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(anonymous).getName(),getAuthorizationHeader(anonymous).getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(randomString.getName(),randomString.getValue()))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testCacheAfterUpdate() throws Exception {
        String userAppointmentsBeforeUpdateResponse = mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsBeforeUpdate = objectMapper.readValue(userAppointmentsBeforeUpdateResponse,
                new TypeReference<List<UserAppointment>>() {});
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        Optional<UserAppointment> byId = userAppointmentRepository.findById(userAppointmentId);
        assertTrue(byId.isPresent());
        UserAppointment updatedUserAppointment = byId.get();
        ZonedDateTime newDate = ZonedDateTime.now(ZoneOffset.UTC).plusDays(15);
        updatedUserAppointment.setDateTime(newDate);
        String json = objectMapper.writeValueAsString(updatedUserAppointment);
        mockMvc.perform(put(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedUserAppointment.getId()))
                .andExpect(jsonPath("$.dateTime").value(updatedUserAppointment.getDateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
        String userAppointmentsAfterUpdateResponse = mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsAfterUpdate = objectMapper.readValue(userAppointmentsAfterUpdateResponse,
                new TypeReference<List<UserAppointment>>() {});
        assertNotEquals(userAppointmentsBeforeUpdate, userAppointmentsAfterUpdate);
    }

    @Test
    void testCacheAfterCreate() throws Exception {
        TestUserDto user = createUser();
        String userAppointmentsBeforeCreateResponse = mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsBeforeCreate = objectMapper.readValue(userAppointmentsBeforeCreateResponse,
                new TypeReference<List<UserAppointment>>() {});
        UserAppointmentByUserCreateDto newUserAppointment = UserAppointmentModels
                .getUserAppointmentByUserDto(product.id());
        String json = objectMapper.writeValueAsString(newUserAppointment);
        mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dateTime").value(newUserAppointment.dateTime()
                        .truncatedTo(ChronoUnit.SECONDS)
                        .format(DateTimeFormatter.ISO_INSTANT)));
        String userAppointmentsAfterCreateResponse = mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(user).getName(), getAuthorizationHeader(user).getValue())
                .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsAfterCreate = objectMapper.readValue(userAppointmentsAfterCreateResponse,
                new TypeReference<List<UserAppointment>>() {});
        assertNotEquals(userAppointmentsBeforeCreate, userAppointmentsAfterCreate);
    }

    @Test
    void testCacheAfterDelete() throws Exception {
        String userAppointmentsBeforeDeleteResponse = mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsBeforeDelete = objectMapper.readValue(userAppointmentsBeforeDeleteResponse,
                new TypeReference<List<UserAppointment>>() {});
        UserAppointmentDto userAppointment = userAppointmentList
                .get(getRandomIndex(userAppointmentList.size()));
        Integer userAppointmentId = userAppointment.getId();
        assertTrue(userAppointmentRepository.existsById(userAppointmentId));
        mockMvc.perform(delete(String.format("%s/%s", UserAppointmentByUserController.REQUEST_MAPPING, userAppointmentId))
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue()))
                .andExpect(status().isOk());
        String userAppointmentsAfterDeleteResponse = mockMvc.perform(get(UserAppointmentByUserController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(getAuthorizationHeader(userDto).getName(), getAuthorizationHeader(userDto).getValue()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        List<UserAppointment> userAppointmentsAfterDelete = objectMapper.readValue(userAppointmentsAfterDeleteResponse,
                new TypeReference<List<UserAppointment>>() {});
        assertNotEquals(userAppointmentsBeforeDelete, userAppointmentsAfterDelete);
    }
}