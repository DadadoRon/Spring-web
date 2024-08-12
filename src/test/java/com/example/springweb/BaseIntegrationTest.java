package com.example.springweb;

import com.example.springweb.controllers.product.AdminProductController;
import com.example.springweb.controllers.product.ProductCreateDto;
import com.example.springweb.controllers.product.ProductDto;
import com.example.springweb.controllers.user.AdminUserController;
import com.example.springweb.controllers.user.TestUserDto;
import com.example.springweb.controllers.user.UserCreateDto;
import com.example.springweb.controllers.user.UserDto;
import com.example.springweb.controllers.userappointment.UserAppointmentDto;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminController;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminCreateDto;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserController;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserCreateDto;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.repository.ProductRepository;
import com.example.springweb.repository.UserAppointmentRepository;
import com.example.springweb.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(PostgreSQLContainerExtension.class)
public class BaseIntegrationTest {
    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public ProductRepository productRepository;

    @Autowired
    public UserAppointmentRepository userAppointmentRepository;

    @Autowired
    public  ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${security.admin.authorization}")
    public String admin;

    @AfterEach
    void delete() {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users", "products", "user_appointments");
    }

    public static String getAuthorizationHeader(User user) {
        return String.format("Basic %s", Base64.getEncoder().encodeToString(
                String.format("%s:%s", user.getEmail(), user.getPassword()).getBytes()));
    }

    public static String getAuthorizationHeader(UserCreateDto user) {
        return String.format("Basic %s", Base64.getEncoder().encodeToString(
                String.format("%s:%s", user.email(), user.password()).getBytes()));
    }

    public static String getAuthorizationHeader(TestUserDto user) {
        return String.format("Basic %s", Base64.getEncoder().encodeToString(
                String.format("%s:%s", user.email(), user.password()).getBytes()));
    }

    public static String getAuthorizationHeader(String admin) {
        return admin;
    }

    public final String randomString() {
        return RandomStringUtils.randomAlphabetic(10);
    }

    public static int getRandomIndex(int listSize) {
        Random random = new Random();
        return random.nextInt(listSize);
    }

    public final User anonymous = UserModels.createUser(null);


    public ProductDto createProduct() throws Exception {
        ProductCreateDto productCreateDto = ProductModels.getProductDto();
        String jsonProduct = objectMapper.writeValueAsString(productCreateDto);
        String jsonResult =  mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                .content(jsonProduct))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(jsonResult, ProductDto.class);
    }

    public List<ProductDto> createProducts() throws Exception {
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductCreateDto> productCreateDtoList = ProductModels.getRandomProductDtoList();
        for (ProductCreateDto productCreateDto : productCreateDtoList) {
            String json = objectMapper.writeValueAsString(productCreateDto);
            String jsonResult =  mockMvc.perform(post(AdminProductController.REQUEST_MAPPING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                    .content(json))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            ProductDto productDto = objectMapper.readValue(jsonResult, ProductDto.class);
            productDtoList.add(productDto);
        }
        return productDtoList;
    }

    public TestUserDto createUser() throws Exception {
        UserCreateDto userCreateDto = UserModels.getUserCreateDto(Role.USER);
        String jsonUser = objectMapper.writeValueAsString(userCreateDto);
        String jsonResult = mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, (admin))
                .content(jsonUser))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        UserDto userDto = objectMapper.readValue(jsonResult, UserDto.class);
        return new TestUserDto(
                userDto.id(),
                userDto.email(),
                userCreateDto.password()
        );
    }

    public List<UserDto> createUsers() throws Exception {
        List<UserDto> userDtoList = new ArrayList<>();
        List<UserCreateDto> userCreateDtoList = UserModels.getRandomUserCreateDtoList();
        for (UserCreateDto userCreateDto : userCreateDtoList) {
            String json = objectMapper.writeValueAsString(userCreateDto);
            String jsonResult = mockMvc.perform(post(String.format("%s/create", AdminUserController.REQUEST_MAPPING))
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                    .content(json))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            UserDto userDto = objectMapper.readValue(jsonResult, UserDto.class);
            userDtoList.add(userDto);
        }
        return userDtoList;
    }

    public List<UserAppointmentDto> createUserAppointments(Integer userId, Integer productId) throws Exception {
        List<UserAppointmentDto> userAppointmentList = new ArrayList<>();
        List<UserAppointmentByAdminCreateDto> userAppointmentByAdminCreateDtos =
                UserAppointmentModels.getRandomUserAppointmentCreateDto(userId, productId);
        for (UserAppointmentByAdminCreateDto userAppointmentByAdminCreateDto : userAppointmentByAdminCreateDtos) {
            String json = objectMapper.writeValueAsString(userAppointmentByAdminCreateDto);
            String jsonResult = mockMvc.perform(post(UserAppointmentByAdminController.REQUEST_MAPPING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(admin))
                    .content(json))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            UserAppointmentDto userAppointmentDto = objectMapper.readValue(jsonResult, UserAppointmentDto.class);
            userAppointmentList.add(userAppointmentDto);
        }
        return userAppointmentList;
    }

    public List<UserAppointmentDto> createUserAppointments(Integer productId, TestUserDto user) throws Exception {
        List<UserAppointmentDto> userAppointmentList = new ArrayList<>();
        List<UserAppointmentByUserCreateDto> userAppointmentByUserCreateDtos =
                UserAppointmentModels.getRandomUserAppointmentCreateDtoByUser(productId);
        for (UserAppointmentByUserCreateDto userAppointmentByUserCreateDto : userAppointmentByUserCreateDtos) {
            String json = objectMapper.writeValueAsString(userAppointmentByUserCreateDto);
            String jsonResult = mockMvc.perform(post(UserAppointmentByUserController.REQUEST_MAPPING)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, getAuthorizationHeader(user))
                    .content(json))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            UserAppointmentDto userAppointmentDto = objectMapper.readValue(jsonResult, UserAppointmentDto.class);
            userAppointmentList.add(userAppointmentDto);
        }
        return  userAppointmentList;
    }
}
