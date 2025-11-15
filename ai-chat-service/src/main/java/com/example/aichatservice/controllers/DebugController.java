package com.example.aichatservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Debug célokra szolgáló végpontok. Cél tesztelni vele a VectorStore működését.
 */
@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
@Slf4j
public class DebugController {

    private final VectorStore vectorStore;

    /**
     * Teszt #1: Alap keresés (bármilyen téma)
     */
    @GetMapping("/search-test")
    public ResponseEntity<?> testSearch(@RequestParam(defaultValue = "könyv") String query,
                                        @RequestParam(defaultValue = "10") int topK,
                                        @RequestParam(defaultValue = "0.0") double threshold) {
        log.info("Test search - query: '{}', topK: {}, threshold: {}", query, topK, threshold);

        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(query)
                        .topK(topK)
                        .similarityThreshold(threshold)  // 0.0 = mindent visszaad!
                        .build()
        );

        log.info("Found {} documents", results.size());

        return ResponseEntity.ok(Map.of(
                "query", query,
                "resultCount", results.size(),
                "results", results.stream()
                        .map(doc -> Map.of(
                                "id", doc.getId(),
                                "score", doc.getScore() != null ? doc.getScore() : "N/A",
                                "textPreview", doc.getText().substring(0, Math.min(200, doc.getText().length())) + "...",
                                "metadata", doc.getMetadata()
                        ))
                        .toList()
        ));
    }

    /**
     * Teszt #2: Összes adat lekérése (ha SimpleVectorStore)
     */
    @GetMapping("/count-all")
    public ResponseEntity<?> countAllDocuments() {
        // Nagyon alacsony threshold-dal mindent visszakap
        List<Document> allDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query("könyv")  // Általános query
                        .topK(1000)      // Nagy szám
                        .similarityThreshold(0.0)  // Minden jó
                        .build()
        );

        log.info("Total documents in VectorStore: {}", allDocs.size());

        return ResponseEntity.ok(Map.of(
                "totalDocuments", allDocs.size(),
                "sampleTitles", allDocs.stream()
                        .limit(10)
                        .map(doc -> doc.getMetadata().get("title"))
                        .toList()
        ));
    }
}
