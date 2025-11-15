package com.example.aichatservice.service;

import com.example.aichatservice.dto.ScoredDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * AI Function a könyvek keresésére a webshop adatbázisában.
 * A funkció szemantikus keresést végez a Vector Store-ban a megadott keresési kifejezés alapján,
 * majd a találatokat tovább finomítja a megadott cím, kategória, szerző és kiadási év alapján.
 */
@Component("searchBooks")
@Description("Könyvek keresése a webshop adatbázisában. Használd ezt a funkciót, ha a felhasználó könyvet keres, ajánlást kér, vagy könyv részleteket szeretne tudni.")
@RequiredArgsConstructor
@Slf4j
public class SearchBooksFunction implements Function<SearchBooksFunction.Request, SearchBooksFunction.Response> {

    private final VectorStore vectorStore;

    private static final double BOOSTED_THRESHOLD = 0.875;

    @Override
    public Response apply(Request request) {
        log.info("AI Function Called: searchBooks(query='{}')",
                request.query);

        try {
            // Vector Store keresés similaritás alapján
            SearchRequest.Builder searchBuilder = SearchRequest.builder()
                    .query(request.query)
                    .topK(5)
                    .similarityThreshold(0.7);

            List<Document> documents = vectorStore.similaritySearch(searchBuilder.build());

            if (documents.isEmpty()) {
                log.info("No books found for query: {}", request.query);
                return new Response(
                        "Sajnos nem találtam megfelelő könyvet a keresési feltételeknek.",
                        0
                );
            }

            // Scoore boosting + Reranking technika
            // Az alkalmazásban csak könyvek és mind informatika jellegű, ezért a hasonlóság alapú keresés sokszor nagyon hasonló
            // score értéket ad vissza több dokumentumnak is. Ezért a metadata mezők alapján további pontokat adunk hozzá a score-hoz,
            // majd a boosted score alapján szűrünk és rendezünk.
            List<ScoredDocument> scoredDocs = documents.stream()
                    .map(doc -> {
                        double baseScore = doc.getScore() != null ? doc.getScore() : 0.0;
                        double boostedScore = baseScore
                                + boostByTitle(doc, request.title)
                                + boostByCategory(doc, request.category)
                                + boostByAuthor(doc, request.author)
                                + boostByPublishYear(doc, request.publishYear);
                        return new ScoredDocument(doc, baseScore, boostedScore);
                    })
                    .filter(sd -> !(sd.getBoostedScore() < BOOSTED_THRESHOLD))
                    .sorted((a, b) -> Double.compare(b.getBoostedScore(), a.getBoostedScore()))
                    .limit(10)
                    .toList();

            List<Document> finalDocuments = scoredDocs.stream()
                    .map(ScoredDocument::getDocument)
                    .toList();

            logScoreDetails(scoredDocs);

            // Találatok formázása
            StringBuilder result = new StringBuilder();
            result.append(String.format("Találtam %d releváns könyvet:\n\n", finalDocuments.size()));

            for (int i = 0; i < finalDocuments.size(); i++) {
                Document doc = finalDocuments.get(i);
                result.append(String.format("## %d. Könyv\n", i + 1));
                result.append(doc.getText());
                result.append("\n---\n\n");
            }

            log.info("Found {} books", finalDocuments.size());
            return new Response(result.toString(), finalDocuments.size());

        } catch (Exception e) {
            log.error("Error searching books", e);
            return new Response("Hiba történt a könyvek keresése közben.", 0);
        }
    }

    /**
     * Cím alapján történő pontnövelés
     * @param doc
     * @param title
     * @return
     */
    private double boostByTitle(Document doc, String title) {
        if (title == null || title.isBlank()) return 0.0;
        String requestTitleLower = title.toLowerCase().trim();
        String docTitle = extractTitle(doc);
        if (docTitle != null) {
            String docTitleLower = docTitle.toLowerCase().trim();
            if (docTitleLower.equals(requestTitleLower)) {
                return 0.5;
            } else if (docTitleLower.contains(requestTitleLower) || requestTitleLower.contains(docTitleLower)) {
                return 0.25;
            }
        }
        return 0.0;
    }

    /**
     * Kategória alapján történő pontnövelés
     * @param doc
     * @param category
     * @return
     */
    private double boostByCategory(Document doc, String category) {
        if (category == null || category.isBlank()) return 0.0;
        String docCategoryName = extractCategoryName(doc);
        if (isCategoryMatch(category.toLowerCase(), docCategoryName)) {
            return 0.25;
        }
        return 0.0;
    }

    /**
     * Szerző alapján történő pontnövelés
     * @param doc
     * @param author
     * @return
     */
    private double boostByAuthor(Document doc, String author) {
        if (author == null || author.isBlank()) return 0.0;
        String requestAuthorLower = author.toLowerCase().trim();
        String docAuthor = extractAuthor(doc);
        if (docAuthor != null) {
            String docAuthorLower = docAuthor.toLowerCase().trim();
            if (docAuthorLower.equals(requestAuthorLower)) {
                return 0.4;
            } else if (docAuthorLower.contains(requestAuthorLower) || requestAuthorLower.contains(docAuthorLower)) {
                return 0.2;
            }
        }
        return 0.0;
    }

    /**
     * Kiadási év alapján történő pontnövelés
     * @param doc
     * @param publishYear
     * @return
     */
    private double boostByPublishYear(Document doc, String publishYear) {
        if (publishYear == null || publishYear.isBlank()) return 0.0;
        Integer docYear = extractPublicationYear(doc);
        try {
            int requestYear = Integer.parseInt(publishYear.trim());
            if (docYear != null && Math.abs(docYear - requestYear) == 0) {
                return 0.3;
            }
        } catch (NumberFormatException e) {
            log.warn("Invalid publishYear format: '{}'", publishYear);
        }
        return 0.0;
    }

    /**
     * Részletes logolás a pontozásról
     */
    private void logScoreDetails(List<ScoredDocument> scoredDocs) {
        log.info("Search results after boosting: {} documents", scoredDocs.size());
        for (int i = 0; i < scoredDocs.size(); i++) {
            ScoredDocument sd = scoredDocs.get(i);
            String title = extractTitle(sd.getDocument());
            String author = extractAuthor(sd.getDocument());
            String category = extractCategoryName(sd.getDocument());
            Integer year = extractPublicationYear(sd.getDocument());

            double boost = sd.getBoostedScore() - sd.getBaseScore();

            log.info("  {}. [Base: {} | Boost: +{} | Final: {}]",
                    i + 1,
                    String.format("%.4f", sd.getBaseScore()),
                    String.format("%.4f", boost),
                    String.format("%.4f", sd.getBoostedScore()));
            log.info("      Title: {} | Author: {} | Category: {} | Year: {}",
                    title, author, category, year);
        }
    }

    /**
     * Cím kinyerése a metadata-ból
     */
    private String extractTitle(Document doc) {
        Object titleObj = doc.getMetadata().get("title");
        return titleObj != null ? titleObj.toString() : null;
    }

    /**
     * Szerző kinyerése a metadata-ból
     */
    private String extractAuthor(Document doc) {
        Object authorObj = doc.getMetadata().get("author");
        return authorObj != null ? authorObj.toString() : null;
    }

    /**
     * Kategória név kinyerése (támogatja a CategoryDto objektumot is)
     */
    private String extractCategoryName(Document doc) {
        Object categoryObj = doc.getMetadata().get("category");

        if (categoryObj == null) return null;

        if (categoryObj instanceof String) {
            return (String) categoryObj;
        } else if (categoryObj instanceof Map) {
            Map<?, ?> categoryMap = (Map<?, ?>) categoryObj;
            Object name = categoryMap.get("name");
            return name != null ? name.toString() : null;
        }

        return categoryObj.toString();
    }

    /**
     * Kiadási év kinyerése
     */
    private Integer extractPublicationYear(Document doc) {
        Object yearObj = doc.getMetadata().get("publicationYear");

        if (yearObj == null) return null;

        if (yearObj instanceof Integer) {
            return (Integer) yearObj;
        } else if (yearObj instanceof String) {
            try {
                return Integer.parseInt((String) yearObj);
            } catch (NumberFormatException e) {
                log.warn("Invalid publication year format: {}", yearObj);
                return null;
            }
        }

        return null;
    }

    /**
     * Kategória egyezés vizsgálata teljes egyezés
     */
    private boolean isCategoryMatch(String queryLower, String categoryName) {
        if (queryLower == null || queryLower.isEmpty() || categoryName == null) {
            return false;
        }

        String categoryLower = categoryName.toLowerCase().trim();

        // Direkt tartalmazza
        return queryLower.contains(categoryLower) || categoryLower.contains(queryLower);
    }

    /**
     * A keresés bemeneti paraméterei
     */
    public record Request(
            @Description("""
                A keresési kifejezés vagy téma (KÖTELEZŐ mező!).
                Ez alapján történik a szemantikus keresés a könyvek között.
                
                Példák helyes használatra:
                - "mesterséges intelligencia" (ha ez a téma érdekel)
                - "Java programozás"
                - "adattudomány könyvek"
                - "Matematika tankönyvek"
                
                FONTOS: Mindig add meg ezt a mezőt! Ha csak kategóriát mond a felhasználó,
                akkor a kategória nevét írd ide query-ként is!
                """)
            String query,

            @Description("""
                Nem kötelező mező: A könyv címe (pl. "Clean Code", "Computer Networking: A Top-Down Approach").
                Használd ezt a mezőt, ha a felhasználó konkrét címet említ a keresés során.
                Ha nem említ ilyet, hagyd üresen.
                """)
            String title,

            @Description("""
                Nem kötelező mező: A könyv kategóriája (pl. "programozás", "mesterséges intelligencia", "matematika").
                Használd ezt a mezőt, ha a felhasználó konkrét kategóriát említ a keresés során.
                Ha nem említ ilyet, hagyd üresen.
                """)
            String category,

            @Description("""
                Nem kötelező mező: A könyv szerzője (pl. "J.K. Rowling", "Robert C. Martin").
                Használd ezt a mezőt, ha a felhasználó konkrét szerzőt említ a keresés során.
                Ha nem említ ilyet, hagyd üresen.
                """)
            String author,

            @Description("""
                Nem kötelező mező: A könyv kiadásának éve(pl 2000, 1998).
                Használd ezt a mezőt, ha a felhasználó kiadás évet említ a keresés során.
                Ha nem említ ilyet, hagyd üresen.
                """)
            String publishYear
    ) {}

    /**
     * A keresés eredményei
     */
    public record Response(
            @Description("A talált könyvek részletes listája vagy hibaüzenet")
            String books,

            @Description("A talált könyvek száma")
            int count
    ) {}
}
