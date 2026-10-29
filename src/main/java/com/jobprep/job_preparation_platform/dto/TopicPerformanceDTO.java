
package com.jobprep.job_preparation_platform.dto;

public class TopicPerformanceDTO {

    private String topic;
    private long attemptedQuestions;
    private long correctAnswers;
    private double accuracyPercentage;

    public TopicPerformanceDTO() {
    }

    public TopicPerformanceDTO(
            String topic,
            long attemptedQuestions,
            long correctAnswers,
            double accuracyPercentage) {
        this.topic = topic;
        this.attemptedQuestions = attemptedQuestions;
        this.correctAnswers = correctAnswers;
        this.accuracyPercentage = accuracyPercentage;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public long getAttemptedQuestions() {
        return attemptedQuestions;
    }

    public void setAttemptedQuestions(long attemptedQuestions) {
        this.attemptedQuestions = attemptedQuestions;
    }

    public long getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(long correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public double getAccuracyPercentage() {
        return accuracyPercentage;
    }

    public void setAccuracyPercentage(double accuracyPercentage) {
        this.accuracyPercentage = accuracyPercentage;
    }
}
