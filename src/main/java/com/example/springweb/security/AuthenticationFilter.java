package com.example.springweb.security;

import com.example.springweb.entity.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationService authenticationService;

    public AuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
         if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            SecurityContextHolder.getContext().setAuthentication(getAuthenticated());
        } else if (request.getRequestURL().toString().contains("/api/")) {
            SecurityContextHolder.getContext().setAuthentication(authenticationService.getAuthenticationToken(request));
        }

        filterChain.doFilter(request, response);
    }

    private AuthenticationToken getAuthenticated() {
        List<SimpleGrantedAuthority> authorities = Stream.of(Role.ADMIN.name())
                .map(SimpleGrantedAuthority::new)
                .toList();
        return AuthenticationToken.builder().authenticated(true)
                .authorities(authorities)
                .build();
    }
}