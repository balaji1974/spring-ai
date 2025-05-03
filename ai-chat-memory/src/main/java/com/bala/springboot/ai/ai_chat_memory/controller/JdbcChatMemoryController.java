package com.bala.springboot.ai.ai_chat_memory.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class JdbcChatMemoryController {

    private final ChatClient chatClient;

    private final ChatMemory chatMemory;
    
    private static final String CONVERSTATION_ID = "MyConversationID-001";

    public JdbcChatMemoryController(ChatMemory chatMemory, ChatModel chatModel) {
    	this.chatMemory = chatMemory;
    	this.chatClient = ChatClient.builder(chatModel)
            .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
            .build();
    }

    // Endpoint to send messages and get responses
    @PostMapping
    public String chat(@RequestBody String request) {
    	return chatClient.prompt()
                .user(request)
                .advisors(a -> a.param(AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, CONVERSTATION_ID))
                .call()
                .content();
    }

    // Endpoint to view conversation history
    @GetMapping("/history")
    public List<Message> getHistory() {
        return chatMemory.get(CONVERSTATION_ID);
    }

    // Endpoint to clear conversation history
    @DeleteMapping("/history")
    public String clearHistory() {
        chatMemory.clear(CONVERSTATION_ID);
        return "Conversation history cleared";
    }
}