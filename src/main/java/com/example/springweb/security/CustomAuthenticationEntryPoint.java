package com.example.springweb.security;

import com.example.springweb.exceptions.ApiError;
import com.example.springweb.exceptions.ApiErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.UNAUTHORIZED.getMessage())
                .debugMessage(authException.getMessage())
                .code(ApiErrorCode.UNAUTHORIZED.name())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (OutputStream responseStream = response.getOutputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(responseStream, apiError);
            responseStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}





