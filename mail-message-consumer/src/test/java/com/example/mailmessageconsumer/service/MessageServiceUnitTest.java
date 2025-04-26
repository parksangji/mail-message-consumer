package com.example.mailmessageconsumer.service;

import com.example.mailmessageconsumer.dto.TextMessage;
import com.example.mailmessageconsumer.entity.MessageLog;
import com.example.mailmessageconsumer.exception.MessageSendingException;
import com.example.mailmessageconsumer.repository.MessageLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MessageServiceUnitTest {

    @InjectMocks
    private MessageService messageService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MessageLogRepository messageLogRepository;

    @Test
    void processMessage_카카오톡_전송_성공_및_로그_저장() {
        // Given
        TextMessage textMessage = new TextMessage();
        textMessage.setRecipient("01012345678");
        textMessage.setContent("카카오톡 메시지 내용");
        textMessage.setMessageType("KAKAO");

        ArgumentCaptor<MessageLog> messageLogCaptor = ArgumentCaptor.forClass(MessageLog.class);

        // When
        messageService.processMessage(textMessage);

        // Then
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class)); // 실제 API 호출은 Mocking
        verify(messageLogRepository, times(1)).save(messageLogCaptor.capture());

        MessageLog savedLog = messageLogCaptor.getValue();
        assertThat(savedLog.getRecipient()).isEqualTo("01012345678");
        assertThat(savedLog.getContent()).isEqualTo("카카오톡 메시지 내용");
        assertThat(savedLog.getMessageType()).isEqualTo("KAKAO");
        assertThat(savedLog.isSuccess()).isTrue();
        assertThat(savedLog.getSentAt()).isNotNull();
        assertThat(savedLog.getErrorMessage()).isNull();
    }

    @Test
    void processMessage_카카오톡_전송_실패_시_로그_저장_및_예외_발생() {
        // Given
        TextMessage textMessage = new TextMessage();
        textMessage.setRecipient("01012345678");
        textMessage.setContent("카카오톡 메시지 내용");
        textMessage.setMessageType("KAKAO");
        RuntimeException kakaoException = new RuntimeException("Kakao API error");

        doThrow(kakaoException).when(restTemplate).postForEntity(anyString(), any(), eq(Void.class));
        ArgumentCaptor<MessageLog> messageLogCaptor = ArgumentCaptor.forClass(MessageLog.class);

        // When
        assertThrows(MessageSendingException.class, () -> messageService.processMessage(textMessage));

        // Then
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class));
        verify(messageLogRepository, times(1)).save(messageLogCaptor.capture());

        MessageLog savedLog = messageLogCaptor.getValue();
        assertThat(savedLog.getRecipient()).isEqualTo("01012345678");
        assertThat(savedLog.getContent()).isEqualTo("카카오톡 메시지 내용");
        assertThat(savedLog.getMessageType()).isEqualTo("KAKAO");
        assertThat(savedLog.isSuccess()).isFalse();
        assertThat(savedLog.getSentAt()).isNotNull();
        assertThat(savedLog.getErrorMessage()).isEqualTo("Kakao API error");
    }

    @Test
    void processMessage_문자_전송_성공_및_로그_저장() {
        // Given
        TextMessage textMessage = new TextMessage();
        textMessage.setRecipient("01098765432");
        textMessage.setContent("문자 메시지 내용");
        textMessage.setMessageType("SMS");

        ArgumentCaptor<MessageLog> messageLogCaptor = ArgumentCaptor.forClass(MessageLog.class);

        // When
        messageService.processMessage(textMessage);

        // Then
        verify(restTemplate, times(1)).postForEntity(anyString(), any(), eq(Void.class)); // 실제 API 호출은 Mocking
        verify(messageLogRepository, times(1)).save(messageLogCaptor.capture());

        MessageLog savedLog = messageLogCaptor.getValue();
        assertThat(savedLog.getRecipient()).isEqualTo("01098765432");
        assertThat(savedLog.getContent()).isEqualTo("문자 메시지 내용");
        assertThat(savedLog.getMessageType()).isEqualTo("SMS");
        assertThat(savedLog.isSuccess()).isTrue();
        assertThat(savedLog.getSentAt()).isNotNull();
        assertThat(savedLog.getErrorMessage()).isNull();
    }

    @Test
    void processMessage_알_수_없는_메시지_타입_처리_및_로그_저장() {
        // Given
        TextMessage textMessage = new TextMessage();
        textMessage.setRecipient("test");
        textMessage.setContent("알 수 없는 메시지");
        textMessage.setMessageType("UNKNOWN");

        ArgumentCaptor<MessageLog> messageLogCaptor = ArgumentCaptor.forClass(MessageLog.class);

        // When
        messageService.processMessage(textMessage);

        // Then
        verify(restTemplate, never()).postForEntity(anyString(), any(), eq(Void.class));
        verify(messageLogRepository, times(1)).save(messageLogCaptor.capture());

        MessageLog savedLog = messageLogCaptor.getValue();
        assertThat(savedLog.getMessageType()).isEqualTo("UNKNOWN");
        assertThat(savedLog.isSuccess()).isFalse(); // 전송 시도 안 했으므로 실패로 간주하거나, 별도 상태를 둘 수 있음
        assertThat(savedLog.getErrorMessage()).isNull(); // 에러 메시지 없을 수도 있음
    }
}
