package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.ContextPromptRecord;


@RestController
public class ContextPromptController {
	
	private final ChatClient chatClient;
	
	public ContextPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    @GetMapping("/openai-contextprompting")
    public String pt_contextual_prompting(@RequestBody ContextPromptRecord contextPrompt) {
        String articleSuggestions = chatClient
                .prompt()
                .user(u -> u.text(contextPrompt.prompt())
                        .param("context", contextPrompt.context()))
                .call()
                .content();
        return articleSuggestions;
    }
}



