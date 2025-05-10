package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class AutomaticPromptController {
	
	private final ChatClient chatClient;
	
	public AutomaticPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
	
    
    @GetMapping("/openai-automaticprompting")
    public String pt_automatic_prompt_engineering() {
        // Generate variants of the same request
        String orderVariants = chatClient
                .prompt("""
                        We have a band merchandise t-shirt webshop, and to train a
                        chatbot we need various ways to order: "One Metallica t-shirt
                        size S". Generate 10 variants, with the same semantics but keep
                        the same meaning.
                        """)
                .options(ChatOptions.builder()
                        .temperature(1.0)  // High temperature for creativity
                        .build())
                .call()
                .content();

        // Evaluate and select the best variant
        String output = chatClient
                .prompt()
                .user(u -> u.text("""
                        Please perform BLEU (Bilingual Evaluation Understudy) evaluation on the following variants:
                        ----
                        {variants}
                        ----

                        Select the instruction candidate with the highest evaluation score.
                        """).param("variants", orderVariants))
                .call()
                .content();
        
        return output;
    }

}



