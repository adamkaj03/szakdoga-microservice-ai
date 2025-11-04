package com.adam.buzas.webshop.main.services;

import com.adam.buzas.webshop.main.dto.BookDataResponseDto;
import com.adam.buzas.webshop.main.util.MultipartInputStreamFileResource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Service
@RequiredArgsConstructor
public class ImageCaptionService {

    private final RestTemplate restTemplate;

    @Value("${ai-service.url}")
    private String aiServiceUrl;

    /**
     * Meghívja a Python AI szolgáltatást, továbbítja a képet,
     * majd visszaadja a detektált könyv adatokat.
     *
     * @param file A feltöltött könyvborító képe.
     * @return AI által extrahált könyv adatok.
     * @throws IOException Ha a fájl olvasása sikertelen.
     */
    public BookDataResponseDto getDescriptionFromImage(MultipartFile file) throws IOException {
        // Multipart/form-data request header beállítása
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Multipart body összeállítása (file -> InputStreamResource)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

        // HTTP entity létrehozása a headerrel és body-val
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // AI service meghívása (POST a /books/extract endpoint-ra)
        ResponseEntity<BookDataResponseDto> response = restTemplate
                .postForEntity(aiServiceUrl + "/books/extract", requestEntity, BookDataResponseDto.class);

        // Hibakezelés: ha nincs válasz vagy body, exception dobása
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IOException("Failed to get book data from AI service, status: " + response.getStatusCode());
        }

        BookDataResponseDto dto = response.getBody();
        if (dto.getError() != null && !dto.getError().isBlank()) {
            throw new IOException("AI service error: " + dto.getError());
        }
        return dto;
    }
}

