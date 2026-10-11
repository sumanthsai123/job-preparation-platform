package com.jobprep.job_preparation_platform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.jobprep.job_preparation_platform.dto.QuestionRequestDTO;
import com.jobprep.job_preparation_platform.dto.QuestionResponseDTO;
import com.jobprep.job_preparation_platform.service.QuestionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    // Get all questions
    @GetMapping
    public ResponseEntity<List<QuestionResponseDTO>> getAllQuestions() {

        return ResponseEntity.ok(
                questionService.getAllQuestions()
        );
    }

    // Get question by ID
    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> getQuestionById(
            @PathVariable int id) {

        return ResponseEntity.ok(
                questionService.getQuestionById(id)
        );
    }

    // Filter questions
    @GetMapping("/filter")
    public ResponseEntity<List<QuestionResponseDTO>> filterQuestions(
            @RequestParam(required = false) String topic,
            @RequestParam(required = false) String difficulty) {

        if (topic != null && difficulty != null) {

            return ResponseEntity.ok(
                    questionService
                            .getQuestionsByTopicAndDifficulty(
                                    topic,
                                    difficulty
                            )
            );
        }

        if (topic != null) {

            return ResponseEntity.ok(
                    questionService.getQuestionsByTopic(topic)
            );
        }

        if (difficulty != null) {

            return ResponseEntity.ok(
                    questionService.getQuestionsByDifficulty(
                            difficulty
                    )
            );
        }

        return ResponseEntity.ok(
                questionService.getAllQuestions()
        );
    }

    // Create question - ADMIN only
    @PostMapping
    public ResponseEntity<QuestionResponseDTO> createQuestion(
            @Valid @RequestBody QuestionRequestDTO request) {

        QuestionResponseDTO createdQuestion =
                questionService.createQuestion(request);

        return ResponseEntity
                .status(201)
                .body(createdQuestion);
    }

    // Update question - ADMIN only
    @PutMapping("/{id}")
    public ResponseEntity<QuestionResponseDTO> updateQuestion(
            @PathVariable int id,
            @Valid @RequestBody QuestionRequestDTO request) {

        QuestionResponseDTO updatedQuestion =
                questionService.updateQuestion(id, request);

        return ResponseEntity.ok(updatedQuestion);
    }

    // Delete question - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable int id) {

        boolean deleted =
                questionService.deleteQuestion(id);

        if (deleted) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}