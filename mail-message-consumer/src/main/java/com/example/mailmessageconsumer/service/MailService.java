package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.MailMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;

    public void processMail(MailMessage mailMessage) {
        LOGGER.info("Processing mail: {}", mailMessage);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailMessage.getRecipient());
        message.setSubject(mailMessage.getSubject());
        message.setText(mailMessage.getBody());

        try {
            mailSender.send(message);
            LOGGER.info("Mail sent successfully to: {}", mailMessage.getRecipient());
        } catch (Exception e) {
            LOGGER.error("Error sending mail to {}: {}", mailMessage.getRecipient(), e.getMessage());
        }
    }
}
