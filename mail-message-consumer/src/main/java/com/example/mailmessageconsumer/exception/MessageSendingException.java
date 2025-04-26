package com.example.mailmessageconsumer.exception;

public class MessageSendingException extends RuntimeException {
    public MessageSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
