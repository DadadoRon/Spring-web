package com.example.springweb.service;

import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.exceptions.UserNotFoundException;
import com.example.springweb.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private final Integer testUserId = 1;
    private final User testUser = User.builder()
            .id(testUserId)
            .firstName("Masha")
            .lastName("Roll")
            .email("Roll@mail.com")
            .password("302302")
            .role(Role.USER)
            .build();
    @Test
    public void userByIdTest() {
        when(userRepository.findByIdRequired(testUserId)).thenReturn(testUser);
        User result = userService.getUserById(testUserId);
        assertNotNull(result);
        assertEquals(testUser.getId(), result.getId());
        assertEquals(testUser.getFirstName(),result.getFirstName());
        verify(userRepository, times(1)).findByIdRequired(testUserId);
    }

    @Test
    public void userByIdNotFoundTest() {
        when(userRepository.findByIdRequired(testUserId))
                .thenThrow(new UserNotFoundException("User not found with id: " + testUserId));
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(testUserId));
        verify(userRepository, times(1)).findByIdRequired(testUserId);
    }

    @Test
    public void createUserTest() {
        when(userRepository.save(testUser)).thenReturn(testUser);
        User result = userService.createUser(testUser);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void updateTest() {
        doNothing().when(userRepository).checkIfExistsById(testUser.getId());
        when(userRepository.save(testUser)).thenReturn(testUser);
        User result = userService.update(testUser);
        assertEquals(testUser, result);
        verify(userRepository, times(1)).checkIfExistsById(1);
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void updateIfIdNotFoundTest() {
        doThrow(new UserNotFoundException("User not found with id: " + testUser.getId()))
                .when(userRepository).checkIfExistsById(testUser.getId());
        assertThrows(UserNotFoundException.class, () -> userService.update(testUser));
        verify(userRepository, times(1)).checkIfExistsById(testUserId);
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userRepository).checkIfExistsById(testUserId);
        doNothing().when(userRepository).deleteById(testUserId);
        userService.deleteUser(testUserId);
        verify(userRepository, times(1)).checkIfExistsById(testUserId);
        verify(userRepository, times(1)).deleteById(testUserId);
    }

    @Test
    public void deleteIfUserIdNotFoundTest() {
        doThrow(new UserNotFoundException("User not found with id: " + testUserId))
                .when(userRepository).checkIfExistsById(testUserId);
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(testUserId));
        verify(userRepository, times(1)).checkIfExistsById(testUserId);
    }
}


