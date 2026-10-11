package com.jobprep.job_preparation_platform.dto;

import java.util.List;

public class QuizResponseDTO {

    private Integer id;
    private String title;
    private String description;
    private String difficulty;
    private List<QuizQuestionResponseDTO> questions;

    public QuizResponseDTO() {}

    public QuizResponseDTO(
            Integer id,
            String title,
            String description,
            String difficulty,
            List<QuizQuestionResponseDTO> questions) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.questions = questions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public List<QuizQuestionResponseDTO> getQuestions() {
        return questions;
    }
     
    public void setQuestions(List<QuizQuestionResponseDTO> questions) {
        this.questions = questions;
    }
}