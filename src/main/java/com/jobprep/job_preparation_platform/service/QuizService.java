package com.jobprep.job_preparation_platform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jobprep.job_preparation_platform.QuestionRepository;
import com.jobprep.job_preparation_platform.Question;
import com.jobprep.job_preparation_platform.Quiz;
import com.jobprep.job_preparation_platform.QuizRepository;
import com.jobprep.job_preparation_platform.dto.QuizQuestionResponseDTO;
import com.jobprep.job_preparation_platform.dto.QuizRequestDTO;
import com.jobprep.job_preparation_platform.dto.QuizResponseDTO;
import com.jobprep.job_preparation_platform.exception.InvalidQuestionException;
import com.jobprep.job_preparation_platform.exception.QuizNotFoundException;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    public QuizService(
            QuizRepository quizRepository,
            QuestionRepository questionRepository) {

        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    // Get all quizzes
    public List<QuizResponseDTO> getAllQuizzes() {

        return quizRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    // Get quiz by ID
    public QuizResponseDTO getQuizById(Integer id) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + id));

        return convertToResponseDTO(quiz);
    }

    // Create quiz
    public QuizResponseDTO createQuiz(QuizRequestDTO request) {

        Quiz quiz = new Quiz();

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDifficulty(request.getDifficulty());

        List<Integer> questionIds = request.getQuestionIds();

        List<Question> questions = questionRepository.findAllById(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new InvalidQuestionException(
                    "One or more question IDs are invalid");
        }

        quiz.setQuestions(questions);

        Quiz savedQuiz = quizRepository.save(quiz);

        return convertToResponseDTO(savedQuiz);
    }

    // Update quiz
    public QuizResponseDTO updateQuiz(
            Integer id,
            QuizRequestDTO request) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException(
                        "Quiz not found with id: " + id));

        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setDifficulty(request.getDifficulty());

        List<Integer> questionIds = request.getQuestionIds();

        List<Question> questions = questionRepository.findAllById(questionIds);

        if (questions.size() != questionIds.size()) {
            throw new InvalidQuestionException(
                    "One or more question IDs are invalid");
        }

        quiz.setQuestions(questions);

        Quiz updatedQuiz = quizRepository.save(quiz);

        return convertToResponseDTO(updatedQuiz);
    }

    // Delete quiz
    public void deleteQuiz(Integer id) {

        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotFoundException("Quiz not found with id: " + id));

        quizRepository.delete(quiz);
    }

    // Entity → Response DTO
    private QuizResponseDTO convertToResponseDTO(Quiz quiz) {

        List<QuizQuestionResponseDTO> questions = quiz.getQuestions()
                .stream()
                .map(question -> new QuizQuestionResponseDTO(
                        question.getId(),
                        question.getQuestionText(),
                        question.getOptionA(),
                        question.getOptionB(),
                        question.getOptionC(),
                        question.getOptionD()))
                .toList();

        return new QuizResponseDTO(
                quiz.getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getDifficulty(),
                questions);
    }
}