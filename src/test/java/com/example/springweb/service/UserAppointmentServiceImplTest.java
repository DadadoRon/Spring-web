package com.example.springweb.service;

import com.example.springweb.entity.Product;
import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.entity.UserAppointment;
import com.example.springweb.exceptions.ProductNotFoundException;
import com.example.springweb.exceptions.UserAppointmentNotFoundException;
import com.example.springweb.exceptions.UserNotFoundException;
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
class UserAppointmentServiceImplTest {

    @Mock
    private UserAppointmentRepository userAppointmentRepository;

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private UserAppointmentServiceImpl userAppointmentService;

    private final Integer testUserAppointmentId = 1;
    private final UserAppointment testUserAppointment = UserAppointment.builder()
            .id(testUserAppointmentId)
            .dateTime(ZonedDateTime.parse("2024-12-03T10:15:30+01:00"))
            .user(new User(1,"T", "T", "k@mai.com", "3001301", Role.USER))
            .product(new Product(1, "KOI", "YUYT", BigDecimal.valueOf(30.10), "img/n4.jpg"))
            .build();

    @Test
    public void createUserAppointmentTest() {
        when(userService.getUserById(testUserAppointment.getUser().getId())).thenReturn(testUserAppointment.getUser());
        when(productService.getProductById(testUserAppointment.getProduct().getId())).thenReturn(testUserAppointment.getProduct());
        when(userAppointmentRepository.save(testUserAppointment)).thenReturn(testUserAppointment);
        UserAppointment result = userAppointmentService.createUserAppointment(testUserAppointment,testUserAppointment.getUser().getId(), testUserAppointment.getProduct().getId());
        assertEquals(testUserAppointment, result);
        assertEquals(testUserAppointment.getUser(), result.getUser());
        assertEquals(testUserAppointment.getProduct(), result.getProduct());

    }
    @Test
    public void createUserAppointmentIfUserByIdNotFoundTest() {
        when(userService.getUserById(testUserAppointment.getUser().getId()))
                .thenThrow(new UserNotFoundException(("User not found with id: " + testUserAppointment.getUser().getId())));
        assertThrows(UserNotFoundException.class, () -> userAppointmentService.createUserAppointment(testUserAppointment, testUserAppointment.getUser().getId(), testUserAppointment.getProduct().getId()));
    }

    @Test
    public void createUserAppointmentIfProductByIdNotFoundTest() {
        when(productService.getProductById(testUserAppointment.getProduct().getId()))
                .thenThrow(new ProductNotFoundException("Product not found with id: " + testUserAppointment.getProduct().getId()));
        assertThrows(ProductNotFoundException.class, () -> userAppointmentService.createUserAppointment(testUserAppointment, testUserAppointment.getUser().getId(), testUserAppointment.getProduct().getId()));
    }

    @Test
    public void updateUserAppointmentTest() {
       when(userAppointmentService.getUserAppointmentById(testUserAppointmentId)).thenReturn(testUserAppointment);
       when(userAppointmentRepository.save(testUserAppointment)).thenReturn(testUserAppointment);
       UserAppointment result = userAppointmentService.updateUserAppointment(testUserAppointment);
       assertEquals(testUserAppointment, result);
    }

    @Test
    public void updateIfUserAppointmentByIdNotFoundTest() {
        when(userAppointmentService.getUserAppointmentById(testUserAppointmentId))
                .thenThrow(new UserAppointmentNotFoundException("UserAppointment not found with id: " + testUserAppointmentId));
        assertThrows(UserAppointmentNotFoundException.class, () -> userAppointmentService.updateUserAppointment(testUserAppointment));
    }

    @Test
    public void deleteUserAppointmentTest() {
        doNothing().when(userAppointmentRepository).checkIfExistsById(testUserAppointmentId);
        doNothing().when(userAppointmentRepository).deleteById(testUserAppointmentId);
        userAppointmentService.deleteUserAppointment(testUserAppointmentId);
    }

    @Test
    public void deleteIfUserAppointmentIdNotFoundTest() {
        doThrow(new UserAppointmentNotFoundException("UserAppointment not found with id: " + testUserAppointmentId))
                .when(userAppointmentRepository).checkIfExistsById(testUserAppointmentId);
        assertThrows(UserAppointmentNotFoundException.class, () -> userAppointmentService.deleteUserAppointment(testUserAppointmentId));
        verify(userAppointmentRepository, times(1)).checkIfExistsById(testUserAppointmentId);
    }
}