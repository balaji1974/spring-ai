package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/openai-zeroshotprompting")
    public Sentiment pt_zero_shot() {
                Sentiment reviewSentiment = chatClient.prompt("""
                Classify movie reviews as POSITIVE, NEUTRAL or NEGATIVE.
                Review: "Her" is a disturbing study revealing the direction
                humanity is headed if AI is allowed to keep evolving,
                unchecked. I wish there were more movies like this masterpiece.
                Sentiment:
                """)
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
