package com.jobprep.job_preparation_platform.dto;

public class QuizResultAnswerDTO {
    private Integer questionId;
    private String questionText;
    private String selectedAnswer;
    private boolean correct;

    public QuizResultAnswerDTO(
            Integer questionId,
            String questionText,
            String selectedAnswer,
            boolean correct) {

        this.questionId = questionId;
        this.questionText = questionText;
        this.selectedAnswer = selectedAnswer;
        this.correct = correct;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
