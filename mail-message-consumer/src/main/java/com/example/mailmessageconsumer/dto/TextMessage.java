package com.example.mailmessageconsumer.dto;

import lombok.Data;

@Data
public class TextMessage {
    private String recipient;
    private String content;
    private String messageType;
}
