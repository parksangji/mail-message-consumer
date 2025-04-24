package com.example.mailmessageconsumer.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(topics = "${kafka.topic.mail}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMailMessage(String message) {
        LOGGER.info("Received mail message: {}", message);
    }

    @KafkaListener(topics = "${kafka.topic.message}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTextMessage(String message) {
        LOGGER.info("Received text message: {}", message);
    }
}
