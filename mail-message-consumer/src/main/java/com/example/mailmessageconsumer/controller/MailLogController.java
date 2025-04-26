package com.example.mailmessageconsumer.controller;

import com.example.mailmessageconsumer.entity.MailLog;
import com.example.mailmessageconsumer.repository.MailLogRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs/mail")
public class MailLogController {

    private final MailLogRepository mailLogRepository;

    public MailLogController(MailLogRepository mailLogRepository) {
        this.mailLogRepository = mailLogRepository;
    }

    @GetMapping("/recipient/{recipient}")
    public ResponseEntity<Page<MailLog>> getMailLogsByRecipient(@PathVariable String recipient, @PageableDefault Pageable pageable) {
        Page<MailLog> logs = mailLogRepository.findAllByRecipient(recipient, pageable);
        return ResponseEntity.ok(logs);
    }
}