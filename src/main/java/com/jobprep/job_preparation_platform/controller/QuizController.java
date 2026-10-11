package com.jobprep.job_preparation_platform.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jobprep.job_preparation_platform.dto.QuizRequestDTO;
import com.jobprep.job_preparation_platform.dto.QuizResponseDTO;
import com.jobprep.job_preparation_platform.service.QuizService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    // Get all quizzes
    @GetMapping
    public ResponseEntity<List<QuizResponseDTO>> getAllQuizzes() {

        return ResponseEntity.ok(
                quizService.getAllQuizzes()
        );
    }

    // Get quiz by ID
    @GetMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> getQuizById(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                quizService.getQuizById(id)
        );
    }

    // Create quiz
    @PostMapping
    public ResponseEntity<QuizResponseDTO> createQuiz(
            @Valid @RequestBody QuizRequestDTO request) {

        QuizResponseDTO response =
                quizService.createQuiz(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // Update quiz
    @PutMapping("/{id}")
    public ResponseEntity<QuizResponseDTO> updateQuiz(
            @PathVariable Integer id,
            @Valid @RequestBody QuizRequestDTO request) {

        return ResponseEntity.ok(
                quizService.updateQuiz(id, request)
        );
    }

    // Delete quiz
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuiz(
            @PathVariable Integer id) {

        quizService.deleteQuiz(id);

        return ResponseEntity.noContent().build();
    }
}