package com.example.aichatservice.controllers;

import com.example.aichatservice.entity.ChatMessage;
import com.example.aichatservice.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Az üzleti logikáért felelős service ezt a controllert használja.
 * TODO: Ha meglesz az api-gateway nyugodtan hívhatja a frontnd közvetlenül ezt is.
 */
@RequestMapping("/api/ai-chat")
@RestController()
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Az adott felhasználó összes chat üzenetét lekéri
     * @param userId A felhasználó azonosítója
     * @return A felhasználó összes chat üzenete
     */
    @GetMapping("/get-all-messages-by-user-id")
    public ResponseEntity<List<ChatMessage>> findAllByUserId(@RequestParam Integer userId) {
        return ResponseEntity.ok(chatMessageService.findAllByUserId(userId));
    }

    /**
     * Új chat üzenetet küld az AI szolgáltatásnak
     * @param message A küldendő chat üzenet
     * @return A válasz chat üzenet az AI szolgáltatástól
     */
    @PostMapping("/send-message")
    public ResponseEntity<ChatMessage> sendMessage(@RequestParam Integer userId, @RequestBody ChatMessage message) {
        return ResponseEntity.ok(chatMessageService.sendMessage(userId, message));
    }
}
