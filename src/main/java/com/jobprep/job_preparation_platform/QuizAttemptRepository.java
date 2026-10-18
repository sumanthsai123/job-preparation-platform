package com.jobprep.job_preparation_platform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuizAttemptRepository
                extends JpaRepository<QuizAttempt, Integer> {
        List<QuizAttempt> findByUserId(Integer userId);

        Page<QuizAttempt> findByUserId(
                        Integer userId,
                        Pageable pageable);
}