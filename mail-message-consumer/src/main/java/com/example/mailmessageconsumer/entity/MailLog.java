package com.example.mailmessageconsumer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "mail_log")
@Data
public class MailLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    private boolean success;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;
}