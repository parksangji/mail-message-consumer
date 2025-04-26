package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.MailMessage;
import com.example.mailmessageconsumer.entity.MailLog;
import com.example.mailmessageconsumer.exception.MailSendingException;
import com.example.mailmessageconsumer.repository.MailLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MailServiceUnitTest {

    @InjectMocks
    private MailService mailService;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MailLogRepository mailLogRepository;

    @Test
    void processMail_메일_전송_성공_및_로그_저장() {
        // Given
        MailMessage mailMessage = new MailMessage();
        mailMessage.setRecipient("test@example.com");
        mailMessage.setSubject("Test Subject");
        mailMessage.setBody("Test Body");

        ArgumentCaptor<MailLog> mailLogCaptor = ArgumentCaptor.forClass(MailLog.class);

        // When
        mailService.processMail(mailMessage);

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(mailLogRepository, times(1)).save(mailLogCaptor.capture());

        MailLog savedLog = mailLogCaptor.getValue();
        assertThat(savedLog.getRecipient()).isEqualTo("test@example.com");
        assertThat(savedLog.getSubject()).isEqualTo("Test Subject");
        assertThat(savedLog.getBody()).isEqualTo("Test Body");
        assertThat(savedLog.isSuccess()).isTrue();
        assertThat(savedLog.getSentAt()).isNotNull();
        assertThat(savedLog.getErrorMessage()).isNull();
    }

    @Test
    void processMail_메일_전송_실패_시_로그_저장_및_예외_발생() {
        // Given
        MailMessage mailMessage = new MailMessage();
        mailMessage.setRecipient("test@example.com");
        mailMessage.setSubject("Test Subject");
        mailMessage.setBody("Test Body");
        RuntimeException mailException = new RuntimeException("Mail sending failed");

        doThrow(mailException).when(mailSender).send(any(SimpleMailMessage.class));
        ArgumentCaptor<MailLog> mailLogCaptor = ArgumentCaptor.forClass(MailLog.class);

        // When
        assertThrows(MailSendingException.class, () -> mailService.processMail(mailMessage));

        // Then
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
        verify(mailLogRepository, times(1)).save(mailLogCaptor.capture());

        MailLog savedLog = mailLogCaptor.getValue();
        assertThat(savedLog.getRecipient()).isEqualTo("test@example.com");
        assertThat(savedLog.getSubject()).isEqualTo("Test Subject");
        assertThat(savedLog.getBody()).isEqualTo("Test Body");
        assertThat(savedLog.isSuccess()).isFalse();
        assertThat(savedLog.getSentAt()).isNotNull();
        assertThat(savedLog.getErrorMessage()).isEqualTo("Mail sending failed");
    }
}