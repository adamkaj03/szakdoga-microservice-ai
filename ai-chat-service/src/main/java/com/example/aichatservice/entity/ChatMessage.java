package com.example.aichatservice.entity;

import com.example.aichatservice.enumerations.ChatSender;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * A ChatMessage entitás, amely a csevegőüzeneteket reprezentálja az adatbázisban.
 */
@Entity
@Data
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Enumerated(EnumType.STRING)
    private ChatSender sender;

    private String text;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
