package com.bala.springboot.ai.ai_chat_ollama_huggingface.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ChatbotConfiguration {
	
    @Bean
    public ChatMemory chatMemory() {
        return new InMemoryChatMemory();
    }
	
	
    @Bean
    //public ChatClient chatClient(ChatClient.Builder builder) {
    public ChatClient chatClient(ChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient
          .builder(chatModel)
          .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
          .build();
        
        
    	
    	/* 1.0.0-M8 */
    	/*
    	return builder
                .defaultAdvisors(new PromptChatMemoryAdvisor( 
                		MessageWindowChatMemory.builder() 
                	    .maxMessages(10)
                	    .build()
                		))
                .build();*/
    }
}