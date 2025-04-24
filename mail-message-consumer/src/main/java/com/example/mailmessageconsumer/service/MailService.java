package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.MailMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    public void processMail(MailMessage mailMessage) {
        LOGGER.info("Processing mail: {}", mailMessage);
    }
}
