package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.dto.EmailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmail(EmailDto email) {
        rabbitTemplate.convertAndSend("email.send", email);
    }

    /**
     * Visszaadja a megadott elérési úton található email sablon tartalmát Stringként.
     */
    public String loadEmailTemplate(String templatePath) {
        try {
            ClassPathResource resource = new ClassPathResource("email-templates/" + templatePath);
            try (InputStream is = resource.getInputStream()) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nem sikerült beolvasni az email sablont", e);
        }
    }
}
