package com.adam.buzas.webshop.main.restcontrollers;

import com.adam.buzas.webshop.main.dto.ChatMessage;
import com.adam.buzas.webshop.main.model.Book;
import com.adam.buzas.webshop.main.services.AiChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: Meg kell szüntetni az osztályt, ha meglesz az api-gateway
 */
@RequestMapping("/api/ai-chat")
@RestController()
@CrossOrigin(origins = {"http://localhost:4200", "https://purple-river-0f0577f03.4.azurestaticapps.net"})
@RequiredArgsConstructor
public class AiChatController {

    private final AiChatService aiChatService;

    /**
     * Az adott felhasználó összes chat üzenetét lekéri
     * @param username A felhasználó neve
     * @return A felhasználó összes chat üzenete
     */
    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getAllMessagesByUser(@RequestParam String username){
        return aiChatService.getAllMessagesByUsername(username);
    }

    /**
     * Új chat üzenetet küld az AI szolgáltatásnak
     * @param message A küldendő chat üzenet
     * @return A válasz chat üzenet az AI szolgáltatástól
     */
    @PostMapping("/send-message")
    public ResponseEntity<ChatMessage> sendMessage(@RequestParam String username, @RequestBody ChatMessage message){
        return aiChatService.sendMessage(username, message);
    }
}
