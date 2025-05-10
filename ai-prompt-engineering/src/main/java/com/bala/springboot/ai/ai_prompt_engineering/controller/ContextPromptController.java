package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ContextPromptController {
	
	private final ChatClient chatClient;
	
	public ContextPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    @GetMapping("/openai-contextprompting")
    public String pt_contextual_prompting() {
        String articleSuggestions = chatClient
                .prompt()
                .user(u -> u.text("""
                        Suggest 3 topics to write an article about with a few lines of
                        description of what this article should contain.

                        Context: {context}
                        """)
                        .param("context", "You are writing for a blog about retro 80's arcade video games."))
                .call()
                .content();
        return articleSuggestions;
    }
}



