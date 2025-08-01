package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.PromptRecord;


@RestController
public class SystemPromptController {
	
	private final ChatClient chatClient;
	
	public SystemPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    record MovieReviews(Movie[] movie_reviews) {
	    enum Sentiment {
	        POSITIVE, NEUTRAL, NEGATIVE
	    }

	    record Movie(Sentiment sentiment, String name) {
	    }
	}

    @GetMapping("/openai-systemprompting-text")
    public String pt_system_prompting_1(@RequestBody PromptRecord prompt) {
    	
        String movieReview = chatClient
                .prompt()
                .system(prompt.systemPrompt())
                .user(prompt.userPrompt())
                .options(ChatOptions.builder()
                        .model("claude-3-7-sonnet-latest")
                        .temperature(1.0)
                        .topK(40)
                        .topP(0.8)
                        .maxTokens(5)
                        .build())
                .call()
                .content();
       return movieReview;
    }
    
    @GetMapping("/openai-systemprompting-json")
    public MovieReviews pt_system_prompting_2(@RequestBody PromptRecord prompt) {
    	MovieReviews movieReviews = chatClient
    	        .prompt()
    	        .system(prompt.systemPrompt())
                .user(prompt.userPrompt())
    	        .call()
    	        .entity(MovieReviews.class);
    	return movieReviews;
    }
    
 

}



