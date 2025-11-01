package com.example.emailservice.service;

import com.example.emailservice.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void send(EmailDto email) {
        log.info("Sending email to: {}", email.getTo());
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email.getTo());
            message.setSubject(email.getSubject());
            message.setText(email.getBody());
            mailSender.send(message);
            log.info("Email sent successfully to: {}", email.getTo());
        } catch (Exception e) {
            log.error("Failed to send email to: {}", email.getTo(), e);
        }
    }
}
