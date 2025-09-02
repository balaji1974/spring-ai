package com.bala.springboot.ai.ai_google_vertex.controller;


import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_google_vertex.service.ChatService;

@RestController
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody @NotNull String prompt) {
        String response = chatService.chat(prompt);
        return ResponseEntity.ok(response);
    }
}