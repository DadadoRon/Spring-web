package com.example.springweb.security;

import com.example.springweb.exceptions.ApiError;
import com.example.springweb.exceptions.ApiErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2) {
        ApiError apiError = ApiError.builder()
                .message(ApiErrorCode.FORBIDDEN.getMessage())
                .debugMessage(arg2.getMessage())
                .code(ApiErrorCode.FORBIDDEN.name())
                .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        try (OutputStream responseStream = response.getOutputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(responseStream, apiError);
            responseStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
