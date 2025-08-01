package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.anthropic.AnthropicChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.StepbackPromptRecord;


@RestController
public class StepbackPromptController {
	
	private final ChatClient chatClient;
	
	public StepbackPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
		AnthropicChatOptions anthropicOptions = AnthropicChatOptions.builder()
                .model("claude-3-7-sonnet-latest")
                .temperature(1.0)
                .topK(40)
                .topP(0.8)
                .maxTokens(1024)
                .build();
		// Mutate to replace the default chatClient with additional defaultOptions 
		// which will be used with the initial prompt and then the next prompt  
		// So building a new chat client with additional default Anthropic Options. 
		// Use this only when you dont have access to change options on the defaults provided
		this.chatClient = chatClient.mutate().defaultOptions(anthropicOptions).build();
    }
    
    @GetMapping("/openai-stepbackprompting")
    public String pt_step_back_prompting(@RequestBody StepbackPromptRecord prompt) {
    	
        String stepBack = chatClient
        		.prompt(prompt.prompt1())
                .call()
                .content();

        // Then use those concepts in the main task
        String story = chatClient
                .prompt()
                .user(u -> u.text(prompt.prompt2())
                        .param("step-back", stepBack))
                .call()
                .content();
        return story;
    }
}



