package com.example.springweb.controllers.user;

import com.example.springweb.entity.User;
import com.example.springweb.exceptions.ApiErrorCode;
import com.example.springweb.exceptions.EntityNotFoundException;
import com.example.springweb.mapper.UserMapper;
import com.example.springweb.security.UserContextHolder;
import com.example.springweb.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static com.example.springweb.controllers.user.CommonUserController.REQUEST_MAPPING;

@RestController
@Tag(name = "Users API")
@RequestMapping(REQUEST_MAPPING)
@RequiredArgsConstructor
public class CommonUserController {
    public static final String REQUEST_MAPPING = "/api/v1/common/users";
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public User findProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new EntityNotFoundException("User is not found", ApiErrorCode.USER_NOT_FOUND);
        } else {
            return UserContextHolder.getUser();
        }
    }

    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserRegisterDto registerDto) {
        User user = userService.registerUser(userMapper.toUserForRegister(registerDto));
        return userMapper.toDto(user);
    }

    @PostMapping("/password/reset")
    public void resetPassword(@Valid @RequestBody UserResetPasswordDto userResetPasswordDto) {
        userService.resetPassword(userResetPasswordDto.email());
    }

    @PostMapping("/password/init")
    public void Password(@Valid @RequestBody PasswordResetTokenDto passwordResetTokenDto) {
        userService.initPassword(passwordResetTokenDto.token(), passwordResetTokenDto.newPassword());
    }
}
