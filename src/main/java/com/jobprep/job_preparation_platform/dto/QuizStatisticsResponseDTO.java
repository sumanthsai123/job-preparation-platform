package com.jobprep.job_preparation_platform.dto;

public class QuizStatisticsResponseDTO {

    private long totalAttempts;
    private long completedAttempts;
    private double averageScorePercentage;
    private double highestScorePercentage;
    private long totalCorrectAnswers;

    public QuizStatisticsResponseDTO(
            long totalAttempts,
            long completedAttempts,
            double averageScorePercentage,
            double highestScorePercentage,
            long totalCorrectAnswers) {

        this.totalAttempts = totalAttempts;
        this.completedAttempts = completedAttempts;
        this.averageScorePercentage = averageScorePercentage;
        this.highestScorePercentage = highestScorePercentage;
        this.totalCorrectAnswers = totalCorrectAnswers;
    }

    public long getTotalAttempts() {
        return totalAttempts;
    }

    public void setTotalAttempts(long totalAttempts) {
        this.totalAttempts = totalAttempts;
    }

    public long getCompletedAttempts() {
        return completedAttempts;
    }

    public void setCompletedAttempts(long completedAttempts) {
        this.completedAttempts = completedAttempts;
    }

    public double getAverageScorePercentage() {
        return averageScorePercentage;
    }

    public void setAverageScorePercentage(
            double averageScorePercentage) {
        this.averageScorePercentage = averageScorePercentage;
    }

    public double getHighestScorePercentage() {
        return highestScorePercentage;
    }

    public void setHighestScorePercentage(
            double highestScorePercentage) {
        this.highestScorePercentage = highestScorePercentage;
    }

    public long getTotalCorrectAnswers() {
        return totalCorrectAnswers;
    }

    public void setTotalCorrectAnswers(long totalCorrectAnswers) {
        this.totalCorrectAnswers = totalCorrectAnswers;
    }
}