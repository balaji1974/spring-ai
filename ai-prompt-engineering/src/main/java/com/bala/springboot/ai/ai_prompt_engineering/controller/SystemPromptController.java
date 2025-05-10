package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @GetMapping("/openai-systemprompting1")
    public String pt_system_prompting_1() {
        String movieReview = chatClient
                .prompt()
                .system("Classify movie reviews as positive, neutral or negative. Only return the label in uppercase.")
                .user("""
                        Review: "Her" is a disturbing study revealing the direction
                        humanity is headed if AI is allowed to keep evolving,
                        unchecked. It's so disturbing I couldn't watch it.

                        Sentiment:
                        """)
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
    
    @GetMapping("/openai-systemprompting2")
    public MovieReviews pt_system_prompting_2() {
    	MovieReviews movieReviews = chatClient
    	        .prompt()
    	        .system("""
    	                Classify movie reviews as positive, neutral or negative. Return
    	                valid JSON.
    	                """)
    	        .user("""
    	                Review: "Her" is a disturbing study revealing the direction
    	                humanity is headed if AI is allowed to keep evolving,
    	                unchecked. It's so disturbing I couldn't watch it.

    	                JSON Response:
    	                """)
    	        .call()
    	        .entity(MovieReviews.class);
    	return movieReviews;
    }
    
 

}



