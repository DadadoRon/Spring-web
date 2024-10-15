package com.example.springweb.security;

import com.example.springweb.controllers.product.AdminProductController;
import com.example.springweb.controllers.user.AdminUserController;
import com.example.springweb.controllers.user.UserController;
import com.example.springweb.controllers.userappointment.byadmin.UserAppointmentByAdminController;
import com.example.springweb.controllers.userappointment.byuser.UserAppointmentByUserController;
import com.example.springweb.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurity {
    private final AuthenticationFilter authenticationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    @SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .addFilterAt(authenticationFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(AdminUserController.REQUEST_MAPPING + "/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(AdminProductController.REQUEST_MAPPING + "/**").hasAuthority(Role.ADMIN.name())
                        .requestMatchers(UserAppointmentByUserController.REQUEST_MAPPING + "/**")
                            .hasAuthority(Role.USER.name())
                        .requestMatchers(UserController.REQUEST_MAPPING + "/**").hasAuthority(Role.USER.name())
                        .requestMatchers(UserAppointmentByAdminController.REQUEST_MAPPING + "/**")
                            .hasAuthority(Role.ADMIN.name())
                        .anyRequest().permitAll()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("X-Backside-Transport", "Content-Type", "authorization",
                "X-Requested-With", "Content-Length", "Accept", "Origin", "Location"));
        configuration.setExposedHeaders(Arrays.asList("X-Backside-Transport", "Content-Type", "authorization",
                "X-Requested-With", "Content-Length", "Accept", "Origin", "Location"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

