package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.EntityNotFoundException;
import com.example.springweb.repository.UserAppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAppointmentServiceTest {

    @Mock
    private UserAppointmentRepository userAppointmentRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private UserAppointmentService userAppointmentService;

    private final Integer testUserAppointmentId = 1;
    private final UserAppointment testUserAppointment = UserAppointment.builder()
            .id(testUserAppointmentId)
            .dateTime(ZonedDateTime.parse("2024-12-03T10:15:30+01:00"))
            .user(new User(2, "T", "T", "k@mai.com", "3001301", Role.USER, "1vd1fv"))
            .product(new Product(3, "KOI", "YUYT", BigDecimal.valueOf(30.10), "img/n4.jpg"))
            .build();

    @Test
    void createUserAppointmentTest() {
        when(userService.findByIdRequired(testUserAppointment.getUser().getId()))
                .thenReturn(testUserAppointment.getUser());
        when(productService.findByIdRequired(testUserAppointment.getProduct().getId()))
                 .thenReturn(testUserAppointment.getProduct());
        when(userAppointmentRepository.save(testUserAppointment))
                .thenReturn(testUserAppointment);
        UserAppointment result = userAppointmentService.createUserAppointment(testUserAppointment,
                testUserAppointment.getUser().getId(), testUserAppointment.getProduct().getId());
        System.out.println(result);
        assertEquals(testUserAppointment, result);
        assertEquals(testUserAppointment.getUser(), result.getUser());
        assertEquals(testUserAppointment.getProduct(), result.getProduct());

    }

    @Test
    void createUserAppointmentIfUserByIdNotFoundTest() {
        when(userService.findByIdRequired(testUserAppointment.getUser().getId()))
                .thenThrow(new EntityNotFoundException(
                        ("User not found with id: " + testUserAppointment.getUser().getId()),
                        ApiErrorCode.USER_NOT_FOUND));
        assertThrows(EntityNotFoundException.class, () -> userAppointmentService.createUserAppointment(
                testUserAppointment, testUserAppointment.getUser().getId(), testUserAppointment.getProduct().getId()));
    }

    @Test
    void createUserAppointmentIfProductByIdNotFoundTest() {
        when(productService.findByIdRequired(testUserAppointment.getProduct().getId()))
                .thenThrow(new EntityNotFoundException(
                        ("Product not found with id: " + testUserAppointment.getProduct().getId()),
                        ApiErrorCode.PRODUCT_NOT_FOUND));
        assertThrows(EntityNotFoundException.class, () -> userAppointmentService.createUserAppointment(
                testUserAppointment, testUserAppointment.getUser().getId(), testUserAppointment.getProduct().getId()));
    }

    @Test
    void updateUserAppointmentTest() {
       when(userAppointmentService.findByIdRequired(testUserAppointmentId)).thenReturn(testUserAppointment);
       when(userAppointmentRepository.save(testUserAppointment)).thenReturn(testUserAppointment);
       UserAppointment result = userAppointmentService.update(testUserAppointment);
       assertEquals(testUserAppointment, result);
    }

    @Test
    void updateIfUserAppointmentByIdNotFoundTest() {
        when(userAppointmentService.findByIdRequired(testUserAppointmentId))
                .thenThrow(new EntityNotFoundException(
                        ("UserAppointment not found with id: " + testUserAppointmentId),
                        ApiErrorCode.APPOINTMENT_NOT_FOUND));
        assertThrows(EntityNotFoundException.class, () -> userAppointmentService
                .update(testUserAppointment));
    }

    @Test
    void deleteUserAppointmentTest() {
        doNothing().when(userAppointmentRepository).checkIfExistsById(testUserAppointmentId);
        doNothing().when(userAppointmentRepository).deleteById(testUserAppointmentId);
        userAppointmentService.delete(testUserAppointmentId);
    }

    @Test
    void deleteIfUserAppointmentIdNotFoundTest() {
        doThrow(new EntityNotFoundException("UserAppointment not found with id: " + testUserAppointmentId,
                ApiErrorCode.APPOINTMENT_NOT_FOUND))
                .when(userAppointmentRepository).checkIfExistsById(testUserAppointmentId);
        assertThrows(EntityNotFoundException.class, () -> userAppointmentService
                .delete(testUserAppointmentId));
        verify(userAppointmentRepository, times(1)).checkIfExistsById(testUserAppointmentId);
    }
}