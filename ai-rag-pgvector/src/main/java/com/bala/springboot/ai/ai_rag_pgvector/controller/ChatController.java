package com.bala.springboot.ai.ai_rag_pgvector.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;


@RestController
public class ChatController {
	private final VectorStore vectorStore;
    private final ChatClient chatClient;
    
    public record ChatMessage(String chatId, String message) {
    }
    
        private static final String SYSTEM_PROMPT = """
            Your are helpful AI assistant who responds to queries primarily based on the documents section below.
            
            Documents:
            
            {documents}
            
            """;

    public ChatController(VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder
        		.defaultAdvisors(new MessageChatMemoryAdvisor(new InMemoryChatMemory()))
        		.build();
    }
    
    @PostMapping("/chat")
    public ChatMessage chat(@RequestBody ChatMessage chatMessageRequest) {
    	
    	String chatId = chatMessageRequest.chatId();
        if (StringUtils.isBlank(chatId)) {
            chatId = UUID.randomUUID().toString();
        }
        final String finalChatId = chatId;
    	
        List<Document> relatedDocuments = vectorStore.similaritySearch(chatMessageRequest.message());
        String documents = relatedDocuments.stream().map(Document::getFormattedContent)
                .collect(Collectors.joining(System.lineSeparator()));

        
        String content= this.chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, finalChatId))
                .system(s -> s.text(SYSTEM_PROMPT).params(Map.of("documents", documents)))
                .user(chatMessageRequest.message())
                .call()
                .content();
        
        return new ChatMessage(finalChatId,content);

    }
	
	
}
