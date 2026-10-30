package com.jobprep.job_preparation_platform.controller;

import java.util.Map;   

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobprep.job_preparation_platform.dto.AuthResponseDTO;
import com.jobprep.job_preparation_platform.dto.LoginRequestDTO;
import com.jobprep.job_preparation_platform.dto.RegisterRequestDTO;
import com.jobprep.job_preparation_platform.dto.RegisterResponseDTO;
import com.jobprep.job_preparation_platform.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request) {

        RegisterResponseDTO response = userService.registerUser(request);

        return ResponseEntity
                .status(201)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequestDTO request) {

        String token = userService.loginUser(request);

        if (token != null) {
            AuthResponseDTO response = new AuthResponseDTO(
                    token,
                    "Bearer");
            return ResponseEntity.ok(response);
        }

        // return ResponseEntity
        //         .status(401)
        //         .body("Invalid email or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid email or password"));
    }
}