package com.jobprep.job_preparation_platform.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobprep.job_preparation_platform.dto.QuizAnswerRequestDTO;
// import com.jobprep.job_preparation_platform.QuizAttempt;
import com.jobprep.job_preparation_platform.dto.QuizAttemptResponseDTO;
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
            @RequestParam Integer userId,
            @RequestParam Integer quizId) {

        QuizAttemptResponseDTO attempt = quizAttemptService.startAttempt(userId, quizId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attempt);
    }

    @PostMapping("/{attemptId}/answers")
    public ResponseEntity<String> submitAnswer(
            @PathVariable Integer attemptId,
            @Valid @RequestBody QuizAnswerRequestDTO request) {

        quizAttemptService.submitAnswer(attemptId, request);

        return ResponseEntity.ok("Answer submitted successfully");
    }

    @PostMapping("/{attemptId}/complete")
    public ResponseEntity<QuizAttemptResponseDTO> completeAttempt(
            @PathVariable Integer attemptId) {

        QuizAttemptResponseDTO result = quizAttemptService.completeAttempt(attemptId);

        return ResponseEntity.ok(result);
    }
}