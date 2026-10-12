package com.jobprep.job_preparation_platform.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(JobNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleJobNotFound(
                        JobNotFoundException ex) {

                ErrorResponse error = new ErrorResponse(
                                404,
                                ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationErrors(
                        MethodArgumentNotValidException ex) {

                Map<String, String> errors = new HashMap<>();

                ex.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> errors.put(
                                                error.getField(),
                                                error.getDefaultMessage()));

                ErrorResponse error = new ErrorResponse(
                                400,
                                "Validation failed",
                                errors);

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(EmailAlreadyExistsException.class)
        public ResponseEntity<ErrorResponse> handleEmailAlreadyExists(
                        EmailAlreadyExistsException ex) {

                ErrorResponse error = new ErrorResponse(
                                409,
                                ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(error);
        }

        @ExceptionHandler(InvalidPasswordException.class)
        public ResponseEntity<ErrorResponse> handleInvalidPassword(
                        InvalidPasswordException ex) {

                ErrorResponse error = new ErrorResponse(401, ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(error);
        }

        @ExceptionHandler(QuizNotFoundException.class)
        public ResponseEntity<ErrorResponse> handleQuizNotFound(
                        QuizNotFoundException ex) {

                ErrorResponse error = new ErrorResponse(404, ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(error);
        }

        @ExceptionHandler(InvalidQuestionException.class)
        public ResponseEntity<ErrorResponse> handleInvalidQuestion(
                        InvalidQuestionException ex) {

                ErrorResponse error = new ErrorResponse(400, ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body(error);
        }

        @ExceptionHandler(DuplicateAnswerException.class)
        public ResponseEntity<ErrorResponse> handleDuplicateAnswer(
                        DuplicateAnswerException ex) {

                ErrorResponse error = new ErrorResponse(
                                409,
                                ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(error);
        }

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<ErrorResponse> handleIllegalState(
                        IllegalStateException ex) {

                ErrorResponse error = new ErrorResponse(
                                409,
                                ex.getMessage());

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(error);
        }

}