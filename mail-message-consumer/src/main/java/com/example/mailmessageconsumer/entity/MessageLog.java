package com.example.mailmessageconsumer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "message_log")
@Data
public class MessageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String messageType; // KAKAO, SMS

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}
