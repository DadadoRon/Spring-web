package com.example.springweb.security;

import com.example.springweb.entity.Role;
import com.example.springweb.entity.User;
import com.example.springweb.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;

    @Value("${security.admin.authorization}")
    private String adminHeader;
    private static final String ADMIN_EMAIL = "admin@mail.com";
    private static final String ADMIN_FIRST_NAME = "Admin";
    private static final String ADMIN_LAST_NAME = "Admin";
    private static final int EXPECTED_AUTH_TOKENS_LENGTH = 2;

    public AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null) return null;
            if (authHeader.equals(adminHeader)) {
                return AuthenticationToken.builder()
                        .authorities(List.of(new SimpleGrantedAuthority(Role.ADMIN.name())))
                        .principal(ADMIN_EMAIL)
                        .user(User.builder()
                                .firstName(ADMIN_FIRST_NAME)
                                .lastName(ADMIN_LAST_NAME)
                                .email(ADMIN_EMAIL)
                                .build())
                        .authHeader(adminHeader)
                        .authenticated(true)
                        .build();
            }
            return getAuthenticationToken(authHeader);
        } catch (Exception e) {
            log.error("Failed to get user profile. Error - {}", e.getMessage());
            return null;
        }
    }

    private AuthenticationToken getAuthenticationToken(String authHeader) {
        try {
            String[] authTokens = authHeader.split(" ");
            if (authTokens.length != EXPECTED_AUTH_TOKENS_LENGTH) return null;

            if ("Basic".equalsIgnoreCase(authTokens[0])) {
                return processBasicAuth(authTokens[1]);
            }

            return null;
        } catch (Exception e) {
            log.error("Can't parse authentication header {}. Error - {}", authHeader, e.getMessage());
            return null;
        }
    }

    private AuthenticationToken processBasicAuth(String authToken) {
        String[] userCredentials = new String(Base64.getDecoder().decode(authToken)).split(":");
        if (userCredentials.length != EXPECTED_AUTH_TOKENS_LENGTH) return null;

        String email = userCredentials[0];
        String password = userCredentials[1];

        User user = userService.getUserByEmail(email);
        String salt = user.getSalt();
        String hashedPassword = BCrypt.hashpw(password, salt);
        boolean isAuthenticated = user != null && Objects.equals(user.getPassword(), hashedPassword);
        if (!isAuthenticated) return null;

        return AuthenticationToken.builder()
                .authorities(List.of(new SimpleGrantedAuthority(user.getRole().name())))
                .principal(user.getEmail())
                .authenticated(true)
                .authHeader("Basic %s".formatted(authToken))
                .user(user)
                .build();
    }
}
