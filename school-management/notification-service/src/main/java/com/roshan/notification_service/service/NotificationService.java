package com.roshan.notification_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    @KafkaListener(topics = "assignment-notification", groupId = "notification-consumer")
    public void consume(String message) {
        log.info("Consumed message: {}", message);
        emailService.sendEmail("test@mail.com","Assignment Scheduled",message);
    }
}
