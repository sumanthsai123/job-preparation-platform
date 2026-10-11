package com.jobprep.job_preparation_platform.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobprep.job_preparation_platform.User;
import com.jobprep.job_preparation_platform.UserRepository;
import com.jobprep.job_preparation_platform.dto.ChangePasswordRequestDTO;
import com.jobprep.job_preparation_platform.dto.LoginRequestDTO;
import com.jobprep.job_preparation_platform.dto.RegisterRequestDTO;
import com.jobprep.job_preparation_platform.dto.RegisterResponseDTO;
import com.jobprep.job_preparation_platform.dto.UpdateProfileRequestDTO;
import com.jobprep.job_preparation_platform.dto.UserResponseDTO;
import com.jobprep.job_preparation_platform.exception.EmailAlreadyExistsException;
import com.jobprep.job_preparation_platform.exception.InvalidPasswordException;

@Service
public class UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;

        public UserService(
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtService jwtService) {

                this.userRepository = userRepository;
                this.passwordEncoder = passwordEncoder;
                this.jwtService = jwtService;
        }

        public RegisterResponseDTO registerUser(RegisterRequestDTO request) {

                if (userRepository.existsByEmail(request.getEmail())) {
                        throw new EmailAlreadyExistsException(
                                        "Email already registered");
                }

                User user = new User();

                user.setName(request.getName());
                user.setEmail(request.getEmail());

                String encodedPassword = passwordEncoder.encode(request.getPassword());

                user.setPassword(encodedPassword);

                user.setRole("USER");

                User savedUser = userRepository.save(user);

                return new RegisterResponseDTO(
                                savedUser.getId(),
                                savedUser.getName(),
                                savedUser.getEmail(),
                                savedUser.getRole());
        }

        public String loginUser(LoginRequestDTO request) {

                User user = userRepository
                                .findByEmail(request.getEmail())
                                .orElse(null);

                if (user == null) {
                        return null;
                }

                boolean passwordMatches = passwordEncoder.matches(
                                request.getPassword(),
                                user.getPassword());

                if (!passwordMatches) {
                        return null;
                }

                return jwtService.generateToken(
                                user.getEmail(),
                                user.getRole());
        }

        public UserResponseDTO getCurrentUser(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return new UserResponseDTO(
                                user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getRole());
        }

        public UserResponseDTO updateCurrentUser(
                        String currentEmail,
                        UpdateProfileRequestDTO request) {

                User user = userRepository.findByEmail(currentEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Check whether the new email belongs to another user
                if (!request.getEmail().equalsIgnoreCase(currentEmail)
                                && userRepository.existsByEmail(request.getEmail())) {

                        throw new EmailAlreadyExistsException(
                                        "Email already registered");
                }

                user.setName(request.getName());
                user.setEmail(request.getEmail());

                User updatedUser = userRepository.save(user);

                return new UserResponseDTO(
                                updatedUser.getId(),
                                updatedUser.getName(),
                                updatedUser.getEmail(),
                                updatedUser.getRole());
        }

        public void changePassword(
                        String currentEmail,
                        ChangePasswordRequestDTO request) {

                User user = userRepository.findByEmail(currentEmail)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Verify current password
                boolean passwordMatches = passwordEncoder.matches(
                                request.getCurrentPassword(),
                                user.getPassword());

                if (!passwordMatches) {
                        throw new InvalidPasswordException(
                                        "Current password is incorrect");
                }

                // Hash the new password
                String encodedNewPassword = passwordEncoder.encode(request.getNewPassword());

                user.setPassword(encodedNewPassword);

                userRepository.save(user);
        }
}