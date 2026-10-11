package com.jobprep.job_preparation_platform;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository
        extends JpaRepository<Question, Integer> {

    List<Question> findByTopicIgnoreCase(String topic);

    List<Question> findByDifficultyIgnoreCase(String difficulty);

    List<Question> findByTopicIgnoreCaseAndDifficultyIgnoreCase(
            String topic,
            String difficulty);
}
