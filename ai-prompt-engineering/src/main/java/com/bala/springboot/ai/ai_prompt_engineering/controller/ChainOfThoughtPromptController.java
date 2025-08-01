package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChainOfThoughtPromptController {
	
	private final ChatClient chatClient;
	
	public ChainOfThoughtPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    
    @GetMapping("/openai-cot-zeroshotprompting")
    public String pt_chain_of_thought_zero_shot(@RequestBody String myPrompt) {
        String output = chatClient
                .prompt(myPrompt)
                .call()
                .content();
        return output;
    }

    @GetMapping("/openai-cot-singlefewshotprompting")
    public String pt_chain_of_thought_singleshot_fewshots(@RequestBody String myPrompt) {
        String output = chatClient
                .prompt(myPrompt)
                .call()
                .content();
        return output;
    }
 
}



