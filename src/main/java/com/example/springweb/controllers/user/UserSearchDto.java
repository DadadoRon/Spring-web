package com.example.springweb.controllers.user;

public record UserSearchDto(
    String firstName,
    String lastName,
    String email
) { }
