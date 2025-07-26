package com.bala.springboot.ai.ai_chat_memory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/in-memory-chat")
public class InMemoryChatMemoryController {
	
	private final ChatClient chatClient;
	
    public InMemoryChatMemoryController(@Value("${app.inmemory.max.message.size}") Integer maxMessageSize, 
    		ChatClient.Builder builder) {
    	
    	
    	// InMemoryChatMemory is Deprecated in favor of MessageWindowChatMemory,
    	// which internally uses InMemoryChatMemoryRepository.
    	// PromptChatMemoryAdvisor : This advisor manages the conversation memory 
    	// using the provided ChatMemory implementation. 
    	// On each interaction, it retrieves the conversation history from the memory 
    	// and appends it to the system prompt as plain text.
    	this.chatClient = builder
                .defaultAdvisors(new PromptChatMemoryAdvisor( 
                		MessageWindowChatMemory.builder() 
                	    .maxMessages(maxMessageSize)
                	    .build()
                		))
                .build();
    }

    // Endpoint to send messages and get responses
    @PostMapping
    public String chat(@RequestBody String request) {
    	return chatClient.prompt()
                .user(request)
                .call()
                .content();
    }

}