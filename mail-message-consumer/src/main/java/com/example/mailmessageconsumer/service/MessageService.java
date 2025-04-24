package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.TextMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@RequiredArgsConstructor
@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final RestTemplate restTemplate;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    @Value("${sms.api.url}")
    private String smsApiUrl;

    public void processMessage(TextMessage textMessage) {
        LOGGER.info("Processing message: {}", textMessage);

        switch (textMessage.getMessageType()) {
            case "kakao" -> sendKakaoMessage(textMessage);
            case "sms" -> sendSmsMessage(textMessage);
            default -> LOGGER.error("Unsupported message type: {}", textMessage.getMessageType());
        }
    }

    private void sendKakaoMessage(TextMessage textMessage) {
        LOGGER.info("Sending Kakao message to {}: {}", textMessage.getRecipient(), textMessage.getContent());
    }

    private void sendSmsMessage(TextMessage textMessage) {
        LOGGER.info("Sending SMS to {}: {}", textMessage.getRecipient(), textMessage.getContent());
    }
}