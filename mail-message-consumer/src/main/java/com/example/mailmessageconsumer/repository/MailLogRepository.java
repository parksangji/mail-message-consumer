package com.example.mailmessageconsumer.repository;

import com.example.mailmessageconsumer.entity.MailLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailLogRepository extends JpaRepository<MailLog, Long> {
}
