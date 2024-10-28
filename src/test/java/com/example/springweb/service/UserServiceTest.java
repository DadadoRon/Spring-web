package com.example.springweb.service;

import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.EntityNotFoundException;
import com.example.springweb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final Integer testUserId = 1;
    private final User testUser = User.builder()
            .id(testUserId)
            .firstName("Masha")
            .lastName("Roll")
            .email("Roll@mail.com")
            .password("302302")
            .role(Role.USER)
            .salt("hbjhvgcfgxfdz")
            .build();

    @Test
    void userByIdTest() {
        when(userRepository.findByIdRequired(testUserId)).thenReturn(testUser);
        User result = userService.findByIdRequired(testUserId);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getFirstName(), result.getFirstName());
        verify(userRepository, times(1)).findByIdRequired(testUserId);
    }

    @Test
    void userByIdNotFoundTest() {
        when(userRepository.findByIdRequired(testUserId))
                .thenThrow(new EntityNotFoundException("User not found with id: " + testUserId,
                        ApiErrorCode.USER_NOT_FOUND));
        assertThrows(EntityNotFoundException.class, () -> userService.findByIdRequired(testUserId));
        verify(userRepository, times(1)).findByIdRequired(testUserId);
    }

    @Test
    void createUserTest() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        User result = userService.create(testUser);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateTest() {
        when(userRepository.findByIdRequired(testUser.getId())).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        User result = userService.update(testUser);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).findByIdRequired(testUserId);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateIfIdNotFoundTest() {
        doThrow(new EntityNotFoundException("User not found with id: " + testUser.getId(), ApiErrorCode.USER_NOT_FOUND))
                .when(userRepository).findByIdRequired(testUser.getId());
        assertThrows(EntityNotFoundException.class, () -> userService.update(testUser));
        verify(userRepository, times(1)).findByIdRequired(testUserId);
    }

    @Test
    void deleteUserTest() {
        doNothing().when(userRepository).checkIfExistsById(testUserId);
        doNothing().when(userRepository).deleteById(testUserId);
        userService.delete(testUserId);
        verify(userRepository, times(1)).checkIfExistsById(testUserId);
        verify(userRepository, times(1)).deleteById(testUserId);
    }

    @Test
    void deleteIfUserIdNotFoundTest() {
        doThrow(new EntityNotFoundException("User not found with id: " + testUserId, ApiErrorCode.USER_NOT_FOUND))
                .when(userRepository).checkIfExistsById(testUserId);
        assertThrows(EntityNotFoundException.class, () -> userService.delete(testUserId));
        verify(userRepository, times(1)).checkIfExistsById(testUserId);
    }
}


