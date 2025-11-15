package com.example.aichatservice.repository;

import com.example.aichatservice.entity.ChatMessage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository  extends CrudRepository<ChatMessage, Integer> {

    // Az adott felhasználó összes üzenetének lekérdezése időrendi sorrendben
    List<ChatMessage> findAllByUserIdOrderByDateTime(Integer userId);

    // Az adott felhasználó legutóbbi N üzenetének lekérdezése időrendi sorrendben
    @Query(value = "SELECT * FROM chat_message WHERE user_id = :userId " +
            "ORDER BY date_time DESC LIMIT :limit", nativeQuery = true)
    List<ChatMessage> findTopByUserIdOrderByDateTimeDesc(
            @Param("userId") Integer userId,
            @Param("limit") int limit
    );
}
