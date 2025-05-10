package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.anthropic.api.AnthropicApi.ThinkingType;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AnthropicChatController {

    private final ChatClient chatClient;

    public AnthropicChatController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    String myPrompt="Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE. "
    		+ " Review: 'Her' is a disturbing study revealing the direction"
    		+ " humanity is headed if AI is allowed to keep evolving,"
    		+ " unchecked. I wish there were more movies like this masterpiece."
    		+ " Sentiment:";

    @GetMapping("/claude-chatoptions")
    public String claude() {
    	AnthropicChatOptions anthropicOptions = AnthropicChatOptions.builder()
    			/*
    			 * Anthropic model to use
    			 */
    	        .model("claude-3-7-sonnet-latest")
    	        
    	        /* 
    	         * Temperature controls the randomness or "creativity" of the model's response. 
    	         * Lower values (0.0-0.3): More deterministic, focused responses. 
    	         * Better for factual questions, classification, or tasks where consistency is critical.
    	         * Medium values (0.4-0.7): Balanced between determinism and creativity. Good for general use cases. 
    	         * Higher values (0.8-1.0): More creative, varied, and potentially surprising responses. 
    	         * Better for creative writing, brainstorming, or generating diverse options.
    	         */
    	        .temperature(1.0)  // Not less than 1.0 for thinking enabled
    	        
    	        /*
    	         * Only sample from the top K options for each subsequent token.
    	         * Use top_k to remove long tail low probability responses.
    	         */
    	         //.topK(40)                 // Anthropic-specific parameter - this must not be used with thinking enabled
    	        
    	        /*
	   	         * limits how many tokens (word pieces) the model can generate in its response.
	   	         * Low values (5-25): For single words, short phrases, or classification labels. 
	   	         * Medium values (50-500): For paragraphs or short explanations.
	   	         * High values (1000+): For long-form content, stories, or complex explanations.
	   	         */
    	        .maxTokens(2000) // Not less than the max budget token used in thinking which is 1024 in our case
    	        
				/*
				 * With the "think" tool, we're giving Claude the ability to include an additional 
				 * thinking step—complete with its own designated space—as part of getting to its final answer.
				 */
    	        .thinking(ThinkingType.ENABLED, 1024)  // Anthropic-specific thinking configuration
    	        .build();
    	
    	
        return chatClient.prompt(myPrompt)
        		.options(anthropicOptions)
                .call()
                .content();
    }
    
}