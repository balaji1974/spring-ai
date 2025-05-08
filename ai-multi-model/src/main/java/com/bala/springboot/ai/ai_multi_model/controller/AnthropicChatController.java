package com.bala.springboot.ai.ai_multi_model.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnthropicChatController {

    private final ChatClient chatClient;

    public AnthropicChatController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/claude")
    public String claude() {
        return chatClient.prompt()
                .user("Tell me an interesting fact about Anthropic")
                .call()
                .content();
    }
    
}