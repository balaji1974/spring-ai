package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OneShotFewShotController {
	
	private final ChatClient chatClient;
	
	
    public OneShotFewShotController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/openai-oneshotfewshotprompting")
    public String pt_ones_shot_few_shots() {
        return chatClient.prompt("""
                Parse a customer's pizza order into valid JSON

                EXAMPLE 1:
                I want a small pizza with cheese, tomato sauce, and pepperoni.
                JSON Response:
                ```
                {
                    "size": "small",
                    "type": "normal",
                    "ingredients": ["cheese", "tomato sauce", "pepperoni"]
                }
                ```

                EXAMPLE 2:
                Can I get a large pizza with tomato sauce, basil and mozzarella.
                JSON Response:
                ```
                {
                    "size": "large",
                    "type": "normal",
                    "ingredients": ["tomato sauce", "basil", "mozzarella"]
                }
                ```

                Now, I would like a large pizza, with the first half cheese and mozzarella.
                And the other tomato sauce, ham and pineapple.
                """)
                .options(ChatOptions.builder()
                        .model("claude-3-7-sonnet-latest")
                        .temperature(0.1)
                        .maxTokens(250)
                        .build())
                .call()
                .content();
    }

}
