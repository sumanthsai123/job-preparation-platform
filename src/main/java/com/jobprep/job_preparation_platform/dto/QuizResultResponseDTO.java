package com.jobprep.job_preparation_platform.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuizResultResponseDTO {

    private Integer attemptId;
    private String quizTitle;
    private String status;
    private Integer score;
    private Integer totalQuestions;
    private List<QuizResultAnswerDTO> answers;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Long durationSeconds;

    public QuizResultResponseDTO(
            Integer attemptId,
            String quizTitle,
            String status,
            Integer score,
            Integer totalQuestions,
            List<QuizResultAnswerDTO> answers) {

        this.attemptId = attemptId;
        this.quizTitle = quizTitle;
        this.status = status;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.answers = answers;
    }

    public Integer getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Integer attemptId) {
        this.attemptId = attemptId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public List<QuizResultAnswerDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuizResultAnswerDTO> answers) {
        this.answers = answers;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }   

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Long getDurationSeconds() {
        return durationSeconds;
    }   

    public void setDurationSeconds(Long durationSeconds) {
        this.durationSeconds = durationSeconds;
    }
}