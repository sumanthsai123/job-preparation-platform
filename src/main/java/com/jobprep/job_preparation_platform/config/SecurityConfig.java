package com.jobprep.job_preparation_platform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.jobprep.job_preparation_platform.security.JwtAuthenticationFilter;
import com.jobprep.job_preparation_platform.service.JwtService;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
            JwtService jwtService) {

        return new JwtAuthenticationFilter(jwtService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtAuthenticationFilter jwtAuthenticationFilter)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exception -> exception

                        // 401 - User is NOT authenticated
                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");

                                    response.getWriter().write(
                                            "{\"status\":401,\"message\":\"Authentication required\"}");
                                })

                        // 403 - User IS authenticated but does not have permission
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.setContentType("application/json");

                                    response.getWriter().write(
                                            "{\"status\":403,\"message\":\"Access denied\"}");
                                }))

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/hello").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        // Anyone authenticated can view jobs
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**")
                        .authenticated()

                        // Only ADMIN can modify jobs
                        .requestMatchers(HttpMethod.POST, "/api/jobs")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/api/questions/**")
                        .authenticated()

                        .requestMatchers(HttpMethod.POST, "/api/questions")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/questions/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/questions/**")
                        .hasRole("ADMIN")

                        // Quizzes autentication endpoints for admin and user
                        .requestMatchers(HttpMethod.GET, "/api/quizzes/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/quizzes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/quizzes/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/quizzes/**").hasRole("ADMIN")

                        .anyRequest().authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}