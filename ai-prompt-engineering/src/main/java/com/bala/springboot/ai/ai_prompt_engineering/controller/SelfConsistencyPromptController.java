package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.ContextPromptRecord;
import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.EmailClassificationRecord;


@RestController
public class SelfConsistencyPromptController {
	
	private final ChatClient chatClient;
	
	public SelfConsistencyPromptController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
    
    
    @PostMapping("/openai-selfconsistencyprompting")
    public String pt_self_consistency(@RequestBody ContextPromptRecord contextPromptRecord) {
        int importantCount = 0;
        int notImportantCount = 0;

        // Run the model 5 times with the same input
        for (int i = 0; i < 5; i++) {
            EmailClassificationRecord output = chatClient
                    .prompt()
                    .user(u -> u.text(contextPromptRecord.context())
                            .param("email", contextPromptRecord.prompt()))
                    .options(ChatOptions.builder()
                            .temperature(1.0)  // Higher temperature for more variation
                            .build())
                    .call()
                    .entity(EmailClassificationRecord.class);

            // Count results
            if (output.classification() == EmailClassificationRecord.Classification.IMPORTANT) {
                importantCount++;
            } 
            else {
                notImportantCount++;
            }
        }

        // Determine the final classification by majority vote
        String finalClassification = importantCount > notImportantCount ? 
                "IMPORTANT" : "NOT IMPORTANT";
        return finalClassification;
    }

}



