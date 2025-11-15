package com.example.aichatservice.config;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Konfiguráció a Vector Store számára.
 * Vector store-ban tároljuk a beágyazott dokumentumokat(könyvek) a hatékony keresés érdekében.
 * Jelenleg egy egyszerű, memóriában tárolt vector store-t használunk.
 * TODO: Később érdemes lehet átgondolni egy perzisztens megoldást (pl. PGVector). Ezzel megőrizhetjük az adatokat újraindítás után is. Így nem kell az induláskor a hosszú időt igénybe vehető szinkronizáció.
 */
@Configuration
public class VectorStoreConfig {

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel) {
        return SimpleVectorStore.builder(embeddingModel).build();
    }
}
