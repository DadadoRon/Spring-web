package com.example.springweb.security;

import com.example.springweb.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationToken implements Authentication {
    @JsonIgnore
    private List<? extends GrantedAuthority> authorities;
    private List<String> groups;
    private String credentials;
    private String principal;
    private boolean authenticated;
    private String details;
    private String authHeader;
    private User user;

    @Override
    public String getName() {

        return principal;
    }
}
