package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.ContextPromptRecord;


@RestController
public class AutomaticPromptController {
	
	private final ChatClient chatClient;
	
	public AutomaticPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
	
    
    @GetMapping("/openai-automaticprompting")
    public String pt_automatic_prompt_engineering(@RequestBody ContextPromptRecord contextPromptRecord) {
        // Generate variants of the same request
        String orderVariants = chatClient
                .prompt(contextPromptRecord.prompt())
                .options(ChatOptions.builder()
                        .temperature(1.0)  // High temperature for creativity
                        .build())
                .call()
                .content();

        // Evaluate and select the best variant
        String output = chatClient
                .prompt()
                .user(u -> u.text(contextPromptRecord.context()).param("variants", orderVariants))
                .call()
                .content();
        
        return output;
    }

}



