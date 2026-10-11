package com.jobprep.job_preparation_platform.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobprep.job_preparation_platform.dto.ChangePasswordRequestDTO;
import com.jobprep.job_preparation_platform.dto.UpdateProfileRequestDTO;
import com.jobprep.job_preparation_platform.dto.UserResponseDTO;
import com.jobprep.job_preparation_platform.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            Authentication authentication) {

        String email = authentication.getName();

        UserResponseDTO user = userService.getCurrentUser(email);

        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponseDTO> updateCurrentUser(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequestDTO request) {

        String email = authentication.getName();

        UserResponseDTO updatedUser = userService.updateCurrentUser(email, request);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/me/password")
    public ResponseEntity<String> changePassword(
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequestDTO request) {

        String email = authentication.getName();

        userService.changePassword(email, request);

        return ResponseEntity.ok("Password changed successfully");
    }
}