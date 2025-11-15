package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.dto.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

/**
 * TODO: Meg kell szüntetni az osztályt, ha meglesz az api-gateway
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AiChatService {

    private final UserService userService;
    private final RestTemplate restTemplate;

    @Value("${ai-chat-service.url}")
    private String aiChatServiceUrl;

    public ResponseEntity<List<ChatMessage>> getAllMessagesByUsername(String username) {
        try {
            Integer userId = getUserId(username);

            String url = aiChatServiceUrl + "/ai-chat/get-all-messages-by-user-id?userId=" + userId;

            return restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    }
            );
        } catch (RestClientException e) {
            log.error("Hiba történt az AI chat szolgáltatás hívásakor a felhasználó {} üzeneteinek lekérésekor: {}",
                    username, e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Collections.emptyList());
        } catch (Exception e) {
            log.error("Váratlan hiba történt a chat üzenetek lekérésekor a felhasználó {} számára: {}",
                    username, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    public ResponseEntity<ChatMessage> sendMessage(String userName, ChatMessage message) {
        try {
            Integer userId = getUserId(userName);

            String url = aiChatServiceUrl + "/ai-chat/send-message?userId=" + userId;
            return restTemplate.postForEntity(url, message, ChatMessage.class);
        } catch (RestClientException e) {
            log.error("Hiba történt az AI chat szolgáltatás hívásakor üzenet küldésekor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(null);
        } catch (Exception e) {
            log.error("Váratlan hiba történt az üzenet küldésekor: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private Integer getUserId(String username) {
        var user = userService.getUserByUsername(username);
        return user.getId();
    }
}
