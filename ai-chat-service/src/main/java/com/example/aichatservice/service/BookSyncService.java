package com.example.aichatservice.service;

import com.example.aichatservice.clients.BookServiceClient;
import com.example.aichatservice.dto.BookDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Szolgáltatás a könyvek szinkronizálására a Book Service-ből a lokális Vector Store-ba.
 * Két módon történik a szinkronizálás:
 * 1. Alkalmazás indításakor egyszer lefut (initial sync)
 * 2. Óránként automatikusan szinkronizál (scheduled sync)
 * TODO: Óránkénti szinkronizálást megszüntetni, helyette a backend küldjön értesítést egy üzenetküldő rendszeren keresztül (RabbitMQ), amikor új könyv kerül felvételre a Book Service-be.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@EnableScheduling
public class BookSyncService {

    private final BookServiceClient bookServiceClient;
    private final VectorStore vectorStore;

    /**
     * Alkalmazás indításakor egyszer lefut
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initialSync() {
        log.info("Starting initial book synchronization...");
        syncBooks();
    }

    /**
     * Óránként automatikusan szinkronizál
     */
    @Scheduled(fixedRate = 3600000)
    public void scheduledSync() {
        log.info("Starting scheduled book synchronization...");
        syncBooks();
    }

    /**
     * Szinkronizálás
     */
    public void syncBooks() {
        try {
            // REST hívás a Book Service felé
            List<BookDto> books = bookServiceClient.getAllBooks();

            if (books == null || books.isEmpty()) {
                log.warn("No books received from Book Service");
                return;
            }

            log.info("Received {} books, converting to documents...", books.size());

            // Konvertálás Document-ekké
            List<Document> documents = books.stream()
                    .map(this::toDocument)
                    .toList();

            // Lokális Vector Store-ba mentés (cache)
            vectorStore.add(documents);

            log.info("Successfully synced {} books to vector store", books.size());

        } catch (Exception e) {
            log.error("Error syncing books from Book Service", e);
        }
    }

    private Document toDocument(BookDto book) {
        String content = String.format(
                """
                Cím: %s
                Szerző: %s
                Kiadás éve: %d
                Ár: %d Ft
                Kategória: %s
                Leírás: %s
                """,
                book.getTitle(),
                book.getAuthor(),
                book.getPublishYear(),
                book.getPrice(),
                book.getCategory().getName(),
                book.getDescription() != null ? book.getDescription() : "Nincs leírás"
        );

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("bookId", book.getId());
        metadata.put("title", book.getTitle());
        metadata.put("author", book.getAuthor());
        metadata.put("publishYear", book.getPublishYear());
        metadata.put("category", book.getCategory());
        metadata.put("price", book.getPrice());

        String documentId = "book-" + book.getId();
        return new Document(documentId, content, metadata);
    }
}
