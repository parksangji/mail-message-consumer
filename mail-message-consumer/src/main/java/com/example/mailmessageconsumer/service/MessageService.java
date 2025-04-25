package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.TextMessage;
import com.example.mailmessageconsumer.entity.MessageLog;
import com.example.mailmessageconsumer.repository.MessageLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


@RequiredArgsConstructor
@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    private final RestTemplate restTemplate;
    private final MessageLogRepository messageLogRepository;

    @Value("${kakao.api.url}")
    private String kakaoApiUrl;

    @Value("${sms.api.url}")
    private String smsApiUrl;

    public void processMessage(TextMessage textMessage) {
        LOGGER.info("Processing message: {}", textMessage);

        MessageLog messageLog = new MessageLog();
        messageLog.setRecipient(textMessage.getRecipient());
        messageLog.setContent(textMessage.getContent());
        messageLog.setMessageType(textMessage.getMessageType());
        messageLog.setSentAt(LocalDateTime.now());

        boolean success = false;
        String errorMessage = null;

        switch (textMessage.getMessageType()) {
            case "kakao" -> {
                try {
                    sendKakaoMessage(textMessage);
                    success = true;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    LOGGER.error("Error sending Kakao message to {}: {}", textMessage.getRecipient(), errorMessage);
                }
            }
            case "sms" -> {
                try {
                    sendSmsMessage(textMessage);
                    success = true;
                } catch (Exception e) {
                    errorMessage = e.getMessage();
                    LOGGER.error("Error sending SMS to {}: {}", textMessage.getRecipient(), errorMessage);
                }
            }
            default -> LOGGER.error("Unsupported message type: {}", textMessage.getMessageType());
        }

        messageLog.setSuccess(success);
        messageLog.setErrorMessage(errorMessage);
        messageLogRepository.save(messageLog);
    }

    private void sendKakaoMessage(TextMessage textMessage) {
        LOGGER.info("Sending Kakao message to {}: {}", textMessage.getRecipient(), textMessage.getContent());
    }

    private void sendSmsMessage(TextMessage textMessage) {
        LOGGER.info("Sending SMS to {}: {}", textMessage.getRecipient(), textMessage.getContent());
    }
}