package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    public void processMessage(TextMessage textMessage) {
        LOGGER.info("Processing message: {}", textMessage);
        // 실제 메시지 전송 로직 구현 (예: 카카오 API, SMS API 연동)
        // 데이터베이스 저장 등 필요한 작업 수행
    }
}
