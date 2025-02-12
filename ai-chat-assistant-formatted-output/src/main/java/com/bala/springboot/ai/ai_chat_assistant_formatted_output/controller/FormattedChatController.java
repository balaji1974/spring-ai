package com.bala.springboot.ai.ai_chat_assistant_formatted_output.controller;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_chat_assistant_formatted_output.model.Article;



@RestController
public class FormattedChatController {
	
	private final ChatClient chatClient;

    public FormattedChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }
    
    @GetMapping("/chat/query")
    List<Article> askQuestion(@RequestParam(name = "question") String question) {
        return chatClient.prompt()
          .user(question)
          .call()
          .entity(new ParameterizedTypeReference<List<Article>>() {});
    }
    
    
}
