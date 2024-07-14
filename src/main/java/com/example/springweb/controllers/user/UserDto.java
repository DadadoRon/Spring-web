package com.example.springweb.controllers.user;

import com.example.springweb.entity.Role;

public record UserDto (
        Integer id,
        String firstName,
        String lastName,
        String email,
        Role role
) {}

