package com.example.aichatservice.service;

import com.example.aichatservice.entity.ChatMessage;
import com.example.aichatservice.enumerations.ChatSender;
import com.example.aichatservice.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A ChatMessageService osztály kezeli a csevegőüzenetek üzleti logikáját,
 * beleértve az üzenetek mentését, lekérését és az AI válaszok generálását.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    private final ChatModel chatModel;

    private final SearchBooksFunction searchBooksFunction;

    @Value("${chat.max-history-messages:10}")
    private int maxHistoryMessages;

    /**
     * Az adott felhasználó összes chat üzenetét lekéri
     * @param userId
     * @return A felhasználó összes chat üzenete
     */
    public List<ChatMessage> findAllByUserId(Integer userId) {
        // ha nincs még üzenet a felhasználóhoz, akkor létrehozunk egy kezdő üzenetet az AI-tól
         if(chatMessageRepository.findAllByUserIdOrderByDateTime(userId).isEmpty()){
             ChatMessage chatMessage = new ChatMessage();
             chatMessage.setUserId(userId);
             chatMessage.setSender(ChatSender.AI);
             chatMessage.setText("Szia! Miben segíthetek?");
             chatMessage.setDateTime(LocalDateTime.now());
             chatMessageRepository.save(chatMessage);
             return List.of(chatMessage);
         } else {
             return chatMessageRepository.findAllByUserIdOrderByDateTime(userId);
         }
    }

    /**
     * Új chat üzenetet küld az AI szolgáltatásnak
     * @param userId
     * @param message A küldendő chat üzenet
     * @return A válasz chat üzenet az AI szolgáltatástól
     */
    @Transactional
    public ChatMessage sendMessage(Integer userId, ChatMessage message) {
        // Felhasználói üzenet mentése, hogy a history meglegyen a későbbi kontextushoz
        message.setUserId(userId);
        chatMessageRepository.save(message);

        return generateResponseMessage(userId, message);
    }

    /**
     * AI válasz generálása a felhasználói üzenetre
     * @param userId
     * @param userMessage Felhasználói chat üzenet
     * @return AI válasz chat üzenet
     */
    private ChatMessage generateResponseMessage(Integer userId, ChatMessage userMessage) {
        try {
            // Legutóbbi üzenetek lekérése
            // ablakozó technika alkalmazása a kontextus méret korlátozása érdekében
            List<ChatMessage> recentMessages = chatMessageRepository
                    .findTopByUserIdOrderByDateTimeDesc(userId, maxHistoryMessages);

            // Prompt építése
            List<Message> messages = buildMessagesWithHistory(recentMessages);
            messages.add(new UserMessage(userMessage.getText()));

            // Beregisztráljuk a searchBooks funkciót az OpenAI modellhez
            OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                    .toolNames("searchBooks")
                    .build();

            Prompt prompt = new Prompt(messages, chatOptions);

            // 3. OpenAI hívás - az AI MAGA DÖNTI EL, hogy meghívja-e a searchBooks funkciót
            log.info("Calling OpenAI with Function Calling enabled...");
            ChatResponse response = chatModel.call(prompt);

            String aiResponse = response.getResult().getOutput().getText();

            // 4. AI válasz mentése
            ChatMessage responseMessage = new ChatMessage();
            responseMessage.setUserId(userId);
            responseMessage.setSender(ChatSender.AI);
            responseMessage.setDateTime(LocalDateTime.now());
            responseMessage.setText(aiResponse);
            chatMessageRepository.save(responseMessage);

            return responseMessage;

        } catch (Exception e) {
            log.error("Error while generating AI response for userId: {}", userId, e);

            // Hibakezelés - felhasználóbarát üzenet
            return createErrorMessage(userId);
        }
    }

    /**
     * Üzenetek összeállítása a rendszer prompttal és a beszélgetési előzményekkel
     * @param recentMessages Legutóbbi chat üzenetek
     * @return Összeállított üzenetek listája
     */
    private List<Message> buildMessagesWithHistory(List<ChatMessage> recentMessages) {
        List<Message> messages = new ArrayList<>();
        // System prompt - AI személyiség és funkciók leírása
        String systemPrompt = """
            Te egy segítőkész vásárlási asszisztens vagy egy Info könyvek.hu webshopban.
            Segíts a vásárlóknak terméket találni, válaszolj a kérdéseikre,
            és adj hasznos ajánlásokat. Légy kedves és professzionális.
            A csevegés során mindig törekedj arra, hogy a vásárlói élmény pozitív legyen.
            Ha a webshop elérhetőségét kérdezik, akkor a cím: 1054 Budapest, Akácfa utca 12.,
            a telefonszám: +36 30 123 4567, az email cím: info@infokonyvek.hu,
            nyitvatartás: H-P 9:00-18:00., SZ 9:00-14:00., V zárva.
            
            FELADATOD:
            - Segíts a vásárlóknak könyvet találni
            - Válaszolj általános vásárlási kérdésekre
            - Légy kedves, barátságos és professzionális
            
            VÁLASZ FORMÁZÁSI ÚTMUTATÓ:
            1. NE használj Markdown formázást (**szöveg**, ## címek)
            2. Használj egyszerű szöveget
            3. Ha kiemelni szeretnél valamit, használj NAGYBETŰT vagy "idézőjelet"
            
            FONTOS SZABÁLYOK:
            1. Ha a felhasználó könyvet keres vagy ajánlást kér, MINDIG használd a 'searchBooks' funkciót!
            2. A 'searchBooks' funkcióból kapott eredményeket használd az ajánlásokhoz
            3. SOHA ne találj ki könyveket - csak a keresési eredményből ajánlj
            4. Mindig jelezd az árat és pár mondatos lírást írj a könyvről
            5. Ha nincs találat, ajánlj alternatívát vagy kérdezz vissza
            6. Ha a felhasználó nem könyvet keres, NE használd a 'searchBooks' funkciót
            
            PÉLDÁK MIKOR HASZNÁLD A searchBooks FUNKCIÓT:
                "Milyen Java könyvetek vannak?"
                "Ajánlasz programozás témában?"
                "Van ai témában könyvetek?"
                "Keresek egy könyvet XY témában"
            
            NE HASZNÁLD A FUNKCIÓT:
                "Szia!"
                "Mennyibe kerül a szállítás?"
                "Mikor érkezik meg a rendelésem?"
                
            === searchBooks FUNKCIÓ HASZNÁLATA: ===
            
            1. A 'searchBooks' QUERY PARAMÉTER KÖTELEZŐ!
                       - Soha ne hagyd üresen!
                       - Használd a felhasználó kulcsszavait
                
            2. HELYES PÉLDÁK:
               "Van mesterséges intelligencia könyvetek?"
               searchBooks(query="mesterséges intelligencia", title=null, category="mesterséges intelligencia", author=null, publishYear=null)
                
               "Milyen Java könyvetek vannak?"
               searchBooks(query="Java programozás", title=null, category="programozás", author=null, publishYear=null)
                
               "Van olyan könyvetek, amit Thomas H. Cormen írt?"
               searchBooks(query="Thomas H. Cormen", title=null, category=null, author="Thomas H. Cormen", publishYear=null)
                
               "Áruljátok a Clean code könyvet?"
               searchBooks(query="Clean Code", title="Clean code", category=null, author=null, publishYear=null)
               
               "Ajánl olyan könyveket, amiket 2000-ben adtak ki?"
               searchBooks(query="2000 publishYear", title=null, category=null, author=null, publishYear=2000)
                
            3. ROSSZ PÉLDÁK (NE ÍGY):
               searchBooks(query="")
               searchBooks(query=null)
                
            → Mindig töltsd ki a query-t!
                """;

            messages.add(new SystemMessage(systemPrompt));

                    // Beszélgetési előzmények hozzáadása
                    recentMessages.stream()
                        .sorted(Comparator.comparing(ChatMessage::getDateTime))
                        .forEach(msg -> {
                            if (msg.getSender() == ChatSender.USER) {
                                messages.add(new UserMessage(msg.getText()));
                            } else {
                                messages.add(new AssistantMessage(msg.getText()));
                            }
                        });

                    return messages;
                }

    /**
     * Hibás válasz üzenet létrehozása és mentése
     * @param userId
     * @return
     */
    private ChatMessage createErrorMessage(Integer userId) {
        ChatMessage errorMessage = new ChatMessage();
        errorMessage.setUserId(userId);
        errorMessage.setSender(ChatSender.AI);
        errorMessage.setDateTime(LocalDateTime.now());
        errorMessage.setText("Sajnálom, jelenleg nem tudok válaszolni. Kérlek, próbáld újra később!");
        chatMessageRepository.save(errorMessage);
        return errorMessage;
    }
}
