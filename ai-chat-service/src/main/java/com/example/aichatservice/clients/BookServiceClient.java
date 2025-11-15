package com.example.aichatservice.clients;

import com.example.aichatservice.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

/**
 * Az osztály felelőssége a Book Service-el való kommunikáció.
 * Külső REST API hívásokat valósít meg a könyvek adatainak lekérésére.
 */
@Component
@Slf4j
public class BookServiceClient {
    private final WebClient webClient;

    public BookServiceClient(@Value("${book-service.url}") String bookServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(bookServiceUrl)
                .build();
    }

    /**
     * Összes könyv lekérése a Book Service-ből.
     *
     * @return A könyvek listája. Hiba esetén üres lista.
     */
    public List<BookDto> getAllBooks() {
        try {
            return webClient.get()
                    .uri("/api/books")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<BookDto>>() {})
                    .block();
        } catch (Exception e) {
            log.error("Error fetching books from Book Service", e);
            return List.of();
        }
    }

    /**
     * Könyv lekérése ID alapján a Book Service-ből.
     *
     * @param id A könyv azonosítója.
     * @return A könyv adatai. Hiba esetén null.
     */
    public BookDto getBookById(Integer id) {
        try {
            return webClient.get()
                    .uri("/api/books/id/{id}", id)
                    .retrieve()
                    .bodyToMono(BookDto.class)
                    .block();
        } catch (Exception e) {
            log.error("Error fetching book {} from Book Service", id, e);
            return null;
        }
    }

}
