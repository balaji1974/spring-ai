package com.bala.springboot.ai.ai_multi_model.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OpenAIChatController {

    private final ChatClient chatClient;

    public OpenAIChatController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/openai")
    public String openAi() {
        return chatClient.prompt()
                .user("Tell me an interesting fact about OpenAI")
                .call()
                .content();
    }
}