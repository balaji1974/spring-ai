package com.bala.springboot.ai.ai_prompt_engineering.controller;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LLMSelectionController {

	private final ChatClient openAIChatClient;
    private final ChatClient anthropicChatClient;
    
    // For updated models list please refer page https://docs.spring.io/spring-ai/reference/api/chat/openai-chat.html
    // Search for spring.ai.openai.chat.options.model
    private Set<String> openAIModels = new HashSet<>(Arrays.asList("gpt-4o", "gpt-4o-mini", "gpt-4-turbo", "gpt-3.5-turbo"));
    
    // For updated models list please refer page https://docs.spring.io/spring-ai/reference/api/chat/anthropic-chat.html
    // Search for spring.ai.anthropic.chat.options.model
    private Set<String> anthropicAIModels = new HashSet<>(Arrays.asList("claude-opus-4-0", 
    		"claude-sonnet-4-0", "claude-3-7-sonnet-latest", "claude-3-5-sonnet-latest", 
    		"claude-3-opus-20240229", "claude-3-sonnet-20240229", "claude-3-haiku-20240307"));
    
	
	public LLMSelectionController(@Qualifier("openAIChatClient") ChatClient openAIChatClient,
            @Qualifier("anthropicChatClient") ChatClient anthropicChatClient) {
		this.openAIChatClient = openAIChatClient;
		this.anthropicChatClient = anthropicChatClient;
	}
	
	@GetMapping("/llm-selector")
    public String llmSelector(@RequestParam String myPrompt, @RequestParam String llmSelector) {
		if ("openai".equalsIgnoreCase(llmSelector)) {
            return openAIChatClient.prompt(myPrompt)
            		
            		.call().content();
        } 
		else if ("anthropic".equalsIgnoreCase(llmSelector)) {
            return anthropicChatClient.prompt(myPrompt)
            		.call().content();
        }
		else return "Invalid ChatClient selected. The only supported values currently are 'openai' and 'anthropi'";
	}
	
	@GetMapping("/llm-and-model-selector")
    public String modelSelector(
    		@RequestParam String myPrompt, 
    		@RequestParam String llmSelector, 
    		@RequestParam String modelSelector) {
		
		if ("openai".equalsIgnoreCase(llmSelector)) {
			if(openAIModels.contains(modelSelector))
	            return openAIChatClient.prompt(myPrompt)
	            		.options(ChatOptions.builder()
	            		        .model(modelSelector)  // Use OpenAI model
	            		        .build())
	            		.call().content();
			else {
				return "The selected model is not suppored at this time";
			}
        } 
		else if ("anthropic".equalsIgnoreCase(llmSelector)) {
			if(anthropicAIModels.contains(modelSelector))
	            return anthropicChatClient.prompt(myPrompt)
	            		.options(ChatOptions.builder()
	            		        .model(modelSelector)  // Use Anthropic's Claude model
	            		        .build())
	            		.call().content();
			else {
				return "The selected model is not suppored at this time";
			}
        }
		else return "Invalid LLM selected. The only supported values currently are 'openai' and 'anthropi'";
	}
}
