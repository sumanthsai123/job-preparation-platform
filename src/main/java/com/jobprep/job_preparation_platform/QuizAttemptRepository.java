package com.jobprep.job_preparation_platform;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository
        extends JpaRepository<QuizAttempt, Integer> {
}