package com.bala.springboot.ai.ai_google_vertex.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatModel chatModel, ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(chatModel)
          .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
          .build();
    }

    public String chat(String prompt) {
        return chatClient.prompt()
          .user(userMessage -> userMessage.text(prompt))
          .call()
          .content();
    }
}