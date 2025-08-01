package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.PromptRecord;


@RestController
public class RolePromptController {
	
	private final ChatClient chatClient;
	
	public RolePromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    @GetMapping("/openai-roleprompting")
    public String role_prompting(@RequestBody PromptRecord prompt) {
        String travelSuggestions = chatClient
                .prompt()
                .system(prompt.systemPrompt())
                .user(prompt.userPrompt())
                .call()
                .content();
        return travelSuggestions;
    }
    
    @GetMapping("/openai-roleprompting-humour")
    public String role_prompting_humour(@RequestBody PromptRecord prompt) {
        String humorousTravelSuggestions = chatClient
                .prompt()
                .system(prompt.systemPrompt())
                .user(prompt.userPrompt())
                .call()
                .content();
        return humorousTravelSuggestions;
    }

}



