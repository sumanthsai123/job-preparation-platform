package com.jobprep.job_preparation_platform.dto;

public class QuizAttemptResponseDTO {

    private Integer id;
    private Integer quizId;
    private String quizTitle;
    private Integer score;
    private Integer totalQuestions;
    private String status;

    public QuizAttemptResponseDTO() {
    }

    public QuizAttemptResponseDTO(
            Integer id,
            Integer quizId,
            String quizTitle,
            Integer score,
            Integer totalQuestions,
            String status) {

        this.id = id;
        this.quizId = quizId;
        this.quizTitle = quizTitle;
        this.score = score;
        this.totalQuestions = totalQuestions;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}