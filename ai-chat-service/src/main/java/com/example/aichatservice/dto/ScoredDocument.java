package com.example.aichatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ai.document.Document;

/**
 * Egy dokumentumot és annak alap és boosted score értékeit tartalmazza.
 */
@Data
@AllArgsConstructor
public class ScoredDocument {
    Document document;
    double baseScore;
    double boostedScore;
}
