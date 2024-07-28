package com.example.springweb.controllers.user;

import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.springweb.controllers.user.UserController.REQUEST_MAPPING;

@RestController
@Tag(name = "Users API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class UserController {
    public static final String REQUEST_MAPPING = "/api/v1/user/users";
    private final UserService userService;


    @PutMapping
    public PasswordUpdateResponseDto updatePassword(@Valid @RequestBody PasswordUpdateDtoByUser passwordUpdateDtoByUser) {
        Integer userId = UserContextHolder.getUser().getId();
        boolean success = userService.updatePassword(
                userId,
                passwordUpdateDtoByUser.oldPassword(),
                passwordUpdateDtoByUser.newPassword()
        );
        String message;
        if (success) {
            message = "Password updated successfully.";
        } else {
            message = "Failed to update password";
        }
        return new PasswordUpdateResponseDto(success, message);
    }
}
