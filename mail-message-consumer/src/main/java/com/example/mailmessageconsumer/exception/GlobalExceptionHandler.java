package com.example.mailmessageconsumer.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MailSendingException.class)
    public ResponseEntity<String> handleMailSendingException(MailSendingException ex) {
        LOGGER.error("Mail sending failed: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MessageSendingException.class)
    public ResponseEntity<String> handleMessageSendingException(MessageSendingException ex) {
        LOGGER.error("Message sending failed: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        LOGGER.error("An unexpected error occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>("An unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
