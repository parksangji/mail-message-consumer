package com.example.mailmessageconsumer.dto;

import lombok.Data;

@Data
public class MailMessage {
    private String recipient;
    private String subject;
    private String body;
}