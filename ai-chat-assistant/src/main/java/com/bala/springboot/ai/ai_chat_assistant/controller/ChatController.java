package com.bala.springboot.ai.ai_chat_assistant.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_chat_assistant.util.Article;


@RestController
class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat/query")
    String generation(String userInput) {
        return this.chatClient.prompt()
            .user(userInput)
            .call()
            .content();
    }
    
    @GetMapping("/chat/query-formatted")
    List<Article> askQuestion(@RequestParam(name = "question") String question) {
        return chatClient.prompt()
          .user(question)
          .call()
          .entity(new ParameterizedTypeReference<List<Article>>() {});
    }
    
}