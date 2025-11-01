package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailDto email) {
        rabbitTemplate.convertAndSend("email.send", email);
    }
}
