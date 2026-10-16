package com.jobprep.job_preparation_platform.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.jobprep.job_preparation_platform.Question;
import com.jobprep.job_preparation_platform.QuestionRepository;
import com.jobprep.job_preparation_platform.Quiz;
import com.jobprep.job_preparation_platform.QuizAttempt;
import com.jobprep.job_preparation_platform.QuizAttemptAnswer;
import com.jobprep.job_preparation_platform.QuizAttemptAnswerRepository;
import com.jobprep.job_preparation_platform.QuizAttemptRepository;
import com.jobprep.job_preparation_platform.QuizRepository;
import com.jobprep.job_preparation_platform.User;
import com.jobprep.job_preparation_platform.UserRepository;
import com.jobprep.job_preparation_platform.dto.QuizAnswerRequestDTO;
import com.jobprep.job_preparation_platform.dto.QuizAttemptResponseDTO;
import com.jobprep.job_preparation_platform.exception.DuplicateAnswerException;
import com.jobprep.job_preparation_platform.exception.InvalidQuestionException;
import com.jobprep.job_preparation_platform.exception.QuizNotFoundException;

@Service
public class QuizAttemptService {

        private final QuizAttemptRepository quizAttemptRepository;
        private final UserRepository userRepository;
        private final QuizRepository quizRepository;
        private final QuizAttemptAnswerRepository quizAttemptAnswerRepository;
        private final QuestionRepository questionRepository;

        public QuizAttemptService(
                        QuizAttemptRepository quizAttemptRepository,
                        UserRepository userRepository,
                        QuizRepository quizRepository,
                        QuizAttemptAnswerRepository quizAttemptAnswerRepository,
                        QuestionRepository questionRepository) {

                this.quizAttemptRepository = quizAttemptRepository;
                this.userRepository = userRepository;
                this.quizRepository = quizRepository;
                this.quizAttemptAnswerRepository = quizAttemptAnswerRepository;
                this.questionRepository = questionRepository;
        }

        public QuizAttemptResponseDTO startAttempt(String email, Integer quizId) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found."));

                Quiz quiz = quizRepository.findById(quizId)
                                .orElseThrow(() -> new QuizNotFoundException(
                                                "Quiz not found with id: " + quizId));

                QuizAttempt attempt = new QuizAttempt();

                attempt.setUser(user);
                attempt.setQuiz(quiz);
                attempt.setScore(0);
                attempt.setTotalQuestions(quiz.getQuestions().size());
                attempt.setStatus("IN_PROGRESS");

                QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

                return new QuizAttemptResponseDTO(
                                savedAttempt.getId(),
                                savedAttempt.getQuiz().getId(),
                                savedAttempt.getQuiz().getTitle(),
                                savedAttempt.getScore(),
                                savedAttempt.getTotalQuestions(),
                                savedAttempt.getStatus());
        }

        public void submitAnswer(
                        String email,
                        Integer attemptId,
                        QuizAnswerRequestDTO request) {

                QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Quiz attempt not found with id: " + attemptId));

                if (!attempt.getUser().getEmail().equalsIgnoreCase(email)) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "You are not allowed to access this quiz attempt.");
                }

                if ("COMPLETED".equals(attempt.getStatus())) {
                        throw new IllegalStateException(
                                        "Cannot submit answers for a completed quiz attempt.");
                }

                Question question = questionRepository.findById(request.getQuestionId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Question not found with id: "
                                                                + request.getQuestionId()));

                boolean questionBelongsToQuiz = attempt.getQuiz()
                                .getQuestions()
                                .stream()
                                .anyMatch(quizQuestion -> quizQuestion.getId().equals(question.getId()));

                if (!questionBelongsToQuiz) {
                        throw new InvalidQuestionException(
                                        "This question does not belong to the quiz for this attempt.");
                }

                if (quizAttemptAnswerRepository
                                .existsByQuizAttemptIdAndQuestionId(
                                                attemptId,
                                                request.getQuestionId())) {

                        throw new DuplicateAnswerException(
                                        "You have already answered this question in this quiz attempt.");
                }

                String selectedAnswer = request.getSelectedAnswer().trim();

                if (!selectedAnswer.matches("[A-Da-d]")) {
                        throw new InvalidQuestionException(
                                        "Invalid answer choice. Please select A, B, C, or D.");
                }

                boolean isCorrect = question.getCorrectAnswer() != null
                                && question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer);

                QuizAttemptAnswer answer = new QuizAttemptAnswer();

                answer.setQuizAttempt(attempt);
                answer.setQuestion(question);
                answer.setSelectedAnswer(selectedAnswer);
                answer.setIsCorrect(isCorrect);

                quizAttemptAnswerRepository.save(answer);
        }

        public QuizAttemptResponseDTO completeAttempt(String email, Integer attemptId) {

                QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Quiz attempt not found with id: " + attemptId));

                // if (!attempt.getUser().getEmail().equalsIgnoreCase(email)) {
                // throw new org.springframework.web.server.ResponseStatusException(
                // org.springframework.http.HttpStatus.FORBIDDEN,
                // "You are not allowed to access this quiz attempt.");
                // }
                if (!attempt.getUser().getEmail().equalsIgnoreCase(email)) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "You are not allowed to access this quiz attempt.");
                }

                if ("COMPLETED".equals(attempt.getStatus())) {
                        throw new IllegalStateException(
                                        "This quiz attempt has already been completed.");
                }

                List<QuizAttemptAnswer> answers = quizAttemptAnswerRepository
                                .findByQuizAttemptId(attemptId);

                int totalQuestions = attempt.getQuiz()
                                .getQuestions()
                                .size();

                if (answers.size() < totalQuestions) {
                        throw new IllegalStateException(
                                        "Please answer all questions before completing the quiz.");
                }

                if (answers.isEmpty()) {
                        throw new IllegalStateException(
                                        "Cannot complete a quiz without answering any questions.");
                }

                int score = 0;

                for (QuizAttemptAnswer answer : answers) {
                        if (Boolean.TRUE.equals(answer.getIsCorrect())) {
                                score++;
                        }
                }

                attempt.setScore(score);
                attempt.setStatus("COMPLETED");

                QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

                return new QuizAttemptResponseDTO(
                                savedAttempt.getId(),
                                savedAttempt.getQuiz().getId(),
                                savedAttempt.getQuiz().getTitle(),
                                savedAttempt.getScore(),
                                savedAttempt.getTotalQuestions(),
                                savedAttempt.getStatus());
        }
}