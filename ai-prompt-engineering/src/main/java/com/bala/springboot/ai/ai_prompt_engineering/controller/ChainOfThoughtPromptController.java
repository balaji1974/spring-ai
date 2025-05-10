package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChainOfThoughtPromptController {
	
	private final ChatClient chatClient;
	
	public ChainOfThoughtPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    
    @GetMapping("/openai-cotzeroshotprompting")
    public String pt_chain_of_thought_zero_shot() {
        String output = chatClient
                .prompt("""
                        When I was 3 years old, my partner was 3 times my age. Now,
                        I am 20 years old. How old is my partner?

                        Let's think step by step.
                        """)
                .call()
                .content();
        return output;
    }

    @GetMapping("/openai-cotsinglefewshotprompting")
    public String pt_chain_of_thought_singleshot_fewshots() {
        String output = chatClient
                .prompt("""
                        Q: When my brother was 2 years old, I was double his age. Now
                        I am 40 years old. How old is my brother? Let's think step
                        by step.
                        A: When my brother was 2 years, I was 2 * 2 = 4 years old.
                        That's an age difference of 2 years and I am older. Now I am 40
                        years old, so my brother is 40 - 2 = 38 years old. The answer
                        is 38.
                        Q: When I was 3 years old, my partner was 3 times my age. Now,
                        I am 20 years old. How old is my partner? Let's think step
                        by step.
                        A:
                        """)
                .call()
                .content();
        return output;
    }
 
}



