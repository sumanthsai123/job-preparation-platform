package com.jobprep.job_preparation_platform.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jobprep.job_preparation_platform.Question;
import com.jobprep.job_preparation_platform.QuestionRepository;
import com.jobprep.job_preparation_platform.dto.QuestionRequestDTO;
import com.jobprep.job_preparation_platform.dto.QuestionResponseDTO;
import com.jobprep.job_preparation_platform.exception.JobNotFoundException;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;

    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    // Get all questions
    public List<QuestionResponseDTO> getAllQuestions() {

        return questionRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Get question by ID
    public QuestionResponseDTO getQuestionById(int id) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Question not found with id: " + id
                        ));

        return convertToResponseDTO(question);
    }

    // Create question
    public QuestionResponseDTO createQuestion(
            QuestionRequestDTO request) {

        Question question = new Question();

        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setExplanation(request.getExplanation());
        question.setDifficulty(request.getDifficulty());
        question.setTopic(request.getTopic());

        Question savedQuestion = questionRepository.save(question);

        return convertToResponseDTO(savedQuestion);
    }

    // Update question
    public QuestionResponseDTO updateQuestion(
            int id,
            QuestionRequestDTO request) {

        Question question = questionRepository.findById(id)
                .orElseThrow(() ->
                        new JobNotFoundException(
                                "Question not found with id: " + id
                        ));

        question.setQuestionText(request.getQuestionText());
        question.setOptionA(request.getOptionA());
        question.setOptionB(request.getOptionB());
        question.setOptionC(request.getOptionC());
        question.setOptionD(request.getOptionD());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setExplanation(request.getExplanation());
        question.setDifficulty(request.getDifficulty());
        question.setTopic(request.getTopic());

        Question updatedQuestion =
                questionRepository.save(question);

        return convertToResponseDTO(updatedQuestion);
    }

    // Delete question
    public boolean deleteQuestion(int id) {

        if (questionRepository.existsById(id)) {

            questionRepository.deleteById(id);
            return true;
        }

        return false;
    }

    // Filter by topic
    public List<QuestionResponseDTO> getQuestionsByTopic(
            String topic) {

        return questionRepository
                .findByTopicIgnoreCase(topic)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Filter by difficulty
    public List<QuestionResponseDTO> getQuestionsByDifficulty(
            String difficulty) {

        return questionRepository
                .findByDifficultyIgnoreCase(difficulty)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Filter by topic + difficulty
    public List<QuestionResponseDTO> getQuestionsByTopicAndDifficulty(
            String topic,
            String difficulty) {

        return questionRepository
                .findByTopicIgnoreCaseAndDifficultyIgnoreCase(
                        topic,
                        difficulty
                )
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    // Entity → Response DTO
    private QuestionResponseDTO convertToResponseDTO(
            Question question) {

        return new QuestionResponseDTO(
                question.getId(),
                question.getQuestionText(),
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD(),
                question.getCorrectAnswer(),
                question.getExplanation(),
                question.getDifficulty(),
                question.getTopic()
        );
    }
}