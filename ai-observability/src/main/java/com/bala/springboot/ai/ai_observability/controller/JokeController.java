package com.bala.springboot.ai.ai_observability.controller;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JokeController {

    private final ChatClient chatClient;

    public JokeController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/joke")
    public Map<String, String> getJoke() {
        String joke = chatClient.prompt()
                .user("Tell me a joke. Keep it short, just the joke.")
                .call()
                .content();
        return Map.of("joke", joke);
    }
}