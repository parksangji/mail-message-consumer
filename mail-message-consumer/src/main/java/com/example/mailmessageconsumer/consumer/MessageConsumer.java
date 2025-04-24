package com.example.mailmessageconsumer.consumer;

import com.example.mailmessageconsumer.dto.MailMessage;
import com.example.mailmessageconsumer.dto.TextMessage;
import com.example.mailmessageconsumer.service.MailService;
import com.example.mailmessageconsumer.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    private final MailService mailService;
    private final MessageService messageService;

    public MessageConsumer(MailService mailService, MessageService messageService) {
        this.mailService = mailService;
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${kafka.topic.mail}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMailMessage(MailMessage mailMessage) {
        LOGGER.info("Received mail message: {}", mailMessage);
        mailService.processMail(mailMessage);
    }

    @KafkaListener(topics = "${kafka.topic.message}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTextMessage(TextMessage textMessage) {
        LOGGER.info("Received text message: {}", textMessage);
        messageService.processMessage(textMessage);
    }
}