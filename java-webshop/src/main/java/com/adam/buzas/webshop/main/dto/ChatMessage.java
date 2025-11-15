package com.adam.buzas.webshop.main.dto;

import com.adam.buzas.webshop.main.enumeration.ChatSender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private ChatSender sender;
    private String text;
    private LocalDateTime dateTime;
}
