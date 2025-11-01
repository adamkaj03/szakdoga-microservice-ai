package com.example.emailservice.messaging;

import com.example.emailservice.dto.EmailDto;
import com.example.emailservice.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailListener {

    private final EmailSenderService senderService;

    @RabbitListener(queues = "email.send")
    public void receiveEmail(EmailDto email) {
        senderService.send(email);
    }
}
