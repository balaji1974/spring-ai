package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RolePromptController {
	
	private final ChatClient chatClient;
	
	public RolePromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    @GetMapping("/openai-roleprompting1")
    public String pt_role_prompting_1() {
        String travelSuggestions = chatClient
                .prompt()
                .system("""
                        I want you to act as a travel guide. I will write to you
                        about my location and you will suggest 3 places to visit near
                        me. In some cases, I will also give you the type of places I
                        will visit.
                        """)
                .user("""
                        My suggestion: "I am in Amsterdam and I want to visit only museums."
                        Travel Suggestions:
                        """)
                .call()
                .content();
        return travelSuggestions;
    }
    
    @GetMapping("/openai-roleprompting2")
    public String pt_role_prompting_2() {
        String humorousTravelSuggestions = chatClient
                .prompt()
                .system("""
                        I want you to act as a travel guide. I will write to you about
                        my location and you will suggest 3 places to visit near me in
                        a humorous style.
                        """)
                .user("""
                        My suggestion: "I am in Amsterdam and I want to visit only museums."
                        Travel Suggestions:
                        """)
                .call()
                .content();
        return humorousTravelSuggestions;
    }

}



