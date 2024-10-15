package com.example.springweb.security;

import com.example.springweb.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserContextHolder {

    public static AuthenticationToken getAuthentication() {
        return (AuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    public static User getUser() {

        return getAuthentication().getUser();
    }

    public static String getAuthHeader() {
        AuthenticationToken authentication = (AuthenticationToken) SecurityContextHolder.getContext()
                .getAuthentication();
        return authentication.getAuthHeader();
    }

    public static String getEmail() {

        return getUser().getEmail();
    }
}

