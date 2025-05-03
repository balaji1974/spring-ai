package com.bala.springboot.ai.ai_chat_memory.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/in-memory-chat")
public class InMemoryChatMemoryController {
	
	private final ChatClient chatClient;
	
	private static final int MAX_MESSAGE_SIZE=10;

    public InMemoryChatMemoryController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new PromptChatMemoryAdvisor(
                		MessageWindowChatMemory.builder()
                	    .maxMessages(MAX_MESSAGE_SIZE)
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