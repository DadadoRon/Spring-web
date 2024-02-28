package com.example.springweb.controllers.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSearchDto {
    private String firstName;
    private String lastName;
    private String email;
}
