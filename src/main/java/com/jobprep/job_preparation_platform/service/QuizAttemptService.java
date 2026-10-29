package com.jobprep.job_preparation_platform.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
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
import com.jobprep.job_preparation_platform.dto.QuizResultAnswerDTO;
import com.jobprep.job_preparation_platform.dto.QuizResultResponseDTO;
import com.jobprep.job_preparation_platform.dto.QuizStatisticsResponseDTO;
import com.jobprep.job_preparation_platform.dto.TopicPerformanceDTO;
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

                attempt.setCompletedAt(LocalDateTime.now());

                QuizAttempt savedAttempt = quizAttemptRepository.save(attempt);

                return new QuizAttemptResponseDTO(
                                savedAttempt.getId(),
                                savedAttempt.getQuiz().getId(),
                                savedAttempt.getQuiz().getTitle(),
                                savedAttempt.getScore(),
                                savedAttempt.getTotalQuestions(),
                                savedAttempt.getStatus());
        }

        public Page<QuizAttemptResponseDTO> getMyAttempts(
                        String email, int page, int size) {

                if (page < 0) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Page number cannot be negative.");
                }

                if (size < 1 || size > 50) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Page size must be between 1 and 50.");
                }

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Pageable pageable = PageRequest.of(
                                page,
                                size,
                                Sort.by(Sort.Direction.DESC, "id"));

                Page<QuizAttempt> attemptPage = quizAttemptRepository.findByUserId(user.getId(), pageable);

                return attemptPage.map(attempt -> {
                        QuizAttemptResponseDTO dto = new QuizAttemptResponseDTO(
                                        attempt.getId(),
                                        attempt.getQuiz().getId(),
                                        attempt.getQuiz().getTitle(),
                                        attempt.getScore(),
                                        attempt.getTotalQuestions(),
                                        attempt.getStatus());

                        dto.setStartedAt(attempt.getStartedAt());
                        dto.setCompletedAt(attempt.getCompletedAt());

                        Long durationSeconds = null;

                        if (attempt.getStartedAt() != null
                                        && attempt.getCompletedAt() != null) {
                                durationSeconds = java.time.Duration.between(
                                                attempt.getStartedAt(),
                                                attempt.getCompletedAt()).getSeconds();
                        }

                        dto.setDurationSeconds(durationSeconds);

                        return dto;
                });
        }

        public QuizResultResponseDTO getAttemptResults(
                        String email, Integer attemptId) {

                QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Quiz attempt not found with id: " + attemptId));

                if (!attempt.getUser().getEmail().equalsIgnoreCase(email)) {
                        throw new ResponseStatusException(
                                        HttpStatus.FORBIDDEN,
                                        "You are not allowed to access this quiz attempt.");
                }

                if (!"COMPLETED".equals(attempt.getStatus())) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Results are available only for completed quiz attempts.");
                }

                List<QuizAttemptAnswer> answers = quizAttemptAnswerRepository.findByQuizAttemptId(attemptId);

                List<QuizResultAnswerDTO> answerDTOs = answers.stream()
                                .map(answer -> new QuizResultAnswerDTO(
                                                answer.getQuestion().getId(),
                                                answer.getQuestion().getQuestionText(),
                                                answer.getSelectedAnswer(),
                                                Boolean.TRUE.equals(answer.getIsCorrect())))
                                .toList();

                QuizResultResponseDTO response = new QuizResultResponseDTO(
                                attempt.getId(),
                                attempt.getQuiz().getTitle(),
                                attempt.getStatus(),
                                attempt.getScore(),
                                attempt.getTotalQuestions(),
                                answerDTOs);

                response.setStartedAt(attempt.getStartedAt());
                response.setCompletedAt(attempt.getCompletedAt());

                if (attempt.getStartedAt() != null
                                && attempt.getCompletedAt() != null) {

                        long durationSeconds = java.time.Duration.between(
                                        attempt.getStartedAt(),
                                        attempt.getCompletedAt()).getSeconds();

                        response.setDurationSeconds(durationSeconds);
                }

                return response;

        }

        public QuizStatisticsResponseDTO getMyStatistics(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                List<QuizAttempt> attempts = quizAttemptRepository.findByUserId(user.getId());

                long totalAttempts = attempts.size();

                List<QuizAttempt> completedAttempts = attempts.stream()
                                .filter(attempt -> "COMPLETED".equals(attempt.getStatus()))
                                .toList();

                long completedCount = completedAttempts.size();

                double totalPercentage = 0.0;
                double highestPercentage = 0.0;
                long totalCorrectAnswers = 0;

                for (QuizAttempt attempt : completedAttempts) {

                        int totalQuestions = attempt.getTotalQuestions() != null
                                        ? attempt.getTotalQuestions()
                                        : 0;

                        int score = attempt.getScore() != null
                                        ? attempt.getScore()
                                        : 0;

                        double percentage = totalQuestions > 0
                                        ? (score * 100.0) / totalQuestions
                                        : 0.0;

                        totalPercentage += percentage;

                        if (percentage > highestPercentage) {
                                highestPercentage = percentage;
                        }

                        totalCorrectAnswers += score;
                }

                double averagePercentage = completedCount > 0
                                ? totalPercentage / completedCount
                                : 0.0;

                return new QuizStatisticsResponseDTO(
                                totalAttempts,
                                completedCount,
                                Math.round(averagePercentage * 100.0) / 100.0,
                                Math.round(highestPercentage * 100.0) / 100.0,
                                totalCorrectAnswers);
        }

        public List<TopicPerformanceDTO> getTopicPerformance(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                // Get all quiz attempts belonging to this user.
                List<QuizAttempt> attempts = quizAttemptRepository.findByUserId(user.getId());

                // Store attempted and correct answer counts for each topic.
                Map<String, long[]> topicCounts = new HashMap<>();

                for (QuizAttempt attempt : attempts) {

                        // Only include completed quiz attempts.
                        if (!"COMPLETED".equals(attempt.getStatus())) {
                                continue;
                        }

                        List<QuizAttemptAnswer> answers = quizAttemptAnswerRepository.findByQuizAttemptId(
                                        attempt.getId());

                        for (QuizAttemptAnswer answer : answers) {

                                String topic = answer.getQuestion().getTopic();

                                if (topic == null || topic.isBlank()) {
                                        topic = "Uncategorized";
                                }

                                long[] counts = topicCounts.computeIfAbsent(
                                                topic, key -> new long[2]);

                                // Index 0 = attempted answers; index 1 = correct answers.
                                counts[0]++;

                                if (Boolean.TRUE.equals(answer.getIsCorrect())) {
                                        counts[1]++;
                                }
                        }
                }

                List<TopicPerformanceDTO> results = new ArrayList<>();

                for (Map.Entry<String, long[]> entry : topicCounts.entrySet()) {

                        String topic = entry.getKey();
                        long attempted = entry.getValue()[0];
                        long correct = entry.getValue()[1];

                        double accuracy = attempted > 0
                                        ? (correct * 100.0) / attempted
                                        : 0.0;

                        accuracy = Math.round(accuracy * 100.0) / 100.0;

                        results.add(new TopicPerformanceDTO(
                                        topic, attempted, correct, accuracy));
                }

                // Return topics in alphabetical order.
                results.sort(Comparator.comparing(
                                TopicPerformanceDTO::getTopic,
                                String.CASE_INSENSITIVE_ORDER));

                return results;
        }

}