package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.MailMessage;
import com.example.mailmessageconsumer.entity.MailLog;
import com.example.mailmessageconsumer.exception.MailSendingException;
import com.example.mailmessageconsumer.repository.MailLogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    private final JavaMailSender mailSender;
    private final MailLogRepository mailLogRepository;

    public void processMail(MailMessage mailMessage) {
        LOGGER.info("Processing mail: {}", mailMessage);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailMessage.getRecipient());
        message.setSubject(mailMessage.getSubject());
        message.setText(mailMessage.getBody());

        MailLog mailLog = new MailLog();
        mailLog.setRecipient(mailMessage.getRecipient());
        mailLog.setSubject(mailMessage.getSubject());
        mailLog.setBody(mailMessage.getBody());
        mailLog.setSentAt(LocalDateTime.now());

        try {
            mailSender.send(message);
            mailLog.setSuccess(true);
            LOGGER.info("Mail sent successfully to: {}", mailMessage.getRecipient());
        } catch (Exception e) {
            mailLog.setSuccess(false);
            mailLog.setErrorMessage(e.getMessage());
            LOGGER.error("Error sending mail to {}: {}", mailMessage.getRecipient(), e.getMessage());
            throw new MailSendingException("Failed to send email to " + mailMessage.getRecipient(), e);
        } finally {
            mailLogRepository.save(mailLog);
        }
    }
}
