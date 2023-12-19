package com.example.springweb.controllers.user;

import com.example.springweb.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
}
