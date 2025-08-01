package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZeroShotController {
	
	private final ChatClient chatClient;
	
	enum Sentiment {
        POSITIVE, NEUTRAL, NEGATIVE
    }

    public ZeroShotController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    String defaultPrompt="""
    		Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE. 
    		A View to a Kill is a James bond movie that did not break any box office records.
    		It has lots of classic bond twists but failed to capture audience attention
    		""";
    @PostMapping("/openai-zeroshotprompting")
    public Sentiment pt_zero_shot(
    		@RequestBody(required=false) String myPrompt) 
    {
	    	if (myPrompt == null || myPrompt.isEmpty()) {
	            // Dynamically determine the default myPrompt
	    		myPrompt = defaultPrompt;
	        }
	    	
            Sentiment reviewSentiment = chatClient.prompt(myPrompt)
            .options(ChatOptions.builder()
                    .model("claude-3-7-sonnet-latest")
                    .temperature(0.1)
                    .maxTokens(5)
                    .build())
            .call()
            .entity(Sentiment.class);

        return reviewSentiment;
    }

}
