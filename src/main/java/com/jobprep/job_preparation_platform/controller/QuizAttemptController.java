package com.jobprep.job_preparation_platform.controller;

import org.springframework.security.core.Authentication;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobprep.job_preparation_platform.dto.QuizAnswerRequestDTO;
// import com.jobprep.job_preparation_platform.QuizAttempt;
import com.jobprep.job_preparation_platform.dto.QuizAttemptResponseDTO;
import com.jobprep.job_preparation_platform.dto.QuizResultResponseDTO;
import com.jobprep.job_preparation_platform.dto.QuizStatisticsResponseDTO;
import com.jobprep.job_preparation_platform.service.QuizAttemptService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quiz-attempts")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    public QuizAttemptController(QuizAttemptService quizAttemptService) {
        this.quizAttemptService = quizAttemptService;
    }

    @PostMapping("/start")
    public ResponseEntity<QuizAttemptResponseDTO> startAttempt(
            Authentication authentication,
            @RequestParam Integer quizId) {

        String email = authentication.getName();

        QuizAttemptResponseDTO attempt = quizAttemptService.startAttempt(email, quizId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attempt);
    }

    @PostMapping("/{attemptId}/answers")
    public ResponseEntity<String> submitAnswer(
            @PathVariable Integer attemptId,
            @Valid @RequestBody QuizAnswerRequestDTO request,
            Authentication authentication) {

        String email = authentication.getName();

        quizAttemptService.submitAnswer(email, attemptId, request);

        return ResponseEntity.ok("Answer submitted successfully");
    }

    @PostMapping("/{attemptId}/complete")
    public ResponseEntity<QuizAttemptResponseDTO> completeAttempt(
            @PathVariable Integer attemptId,
            Authentication authentication) {

        String email = authentication.getName();

        QuizAttemptResponseDTO result = quizAttemptService.completeAttempt(email, attemptId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-attempts")
    public ResponseEntity<Page<QuizAttemptResponseDTO>> getMyAttempts(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        String email = authentication.getName();

        Page<QuizAttemptResponseDTO> attempts = quizAttemptService.getMyAttempts(email, page, size);

        return ResponseEntity.ok(attempts);
    }

    @GetMapping("/{attemptId}/results")
    public ResponseEntity<QuizResultResponseDTO> getAttemptResults(
            @PathVariable Integer attemptId,
            Authentication authentication) {

        String email = authentication.getName();

        QuizResultResponseDTO result = quizAttemptService.getAttemptResults(email, attemptId);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/my-statistics")
    public ResponseEntity<QuizStatisticsResponseDTO> getMyStatistics(
            Authentication authentication) {

        String email = authentication.getName();

        QuizStatisticsResponseDTO statistics = quizAttemptService.getMyStatistics(email);

        return ResponseEntity.ok(statistics);
    }

}