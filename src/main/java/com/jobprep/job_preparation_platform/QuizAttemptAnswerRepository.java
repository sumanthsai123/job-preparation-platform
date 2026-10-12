package com.jobprep.job_preparation_platform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptAnswerRepository
        extends JpaRepository<QuizAttemptAnswer, Integer> {

    List<QuizAttemptAnswer> findByQuizAttemptId(Integer quizAttemptId);

    boolean existsByQuizAttemptIdAndQuestionId(
            Integer quizAttemptId,
            Integer questionId);
            
}