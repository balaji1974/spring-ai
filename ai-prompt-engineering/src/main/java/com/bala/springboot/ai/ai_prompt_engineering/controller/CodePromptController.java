package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.ContextPromptRecord;


@RestController
public class CodePromptController {
	
	private final ChatClient chatClient;
	
	public CodePromptController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
	
    
    @GetMapping("/openai-codeprompting")
    public String pt_code_prompting_writing_code(@RequestBody String promptText) {
        String bashScript = chatClient
                .prompt(promptText)
                .options(ChatOptions.builder()
                        .temperature(0.1)  // Low temperature for deterministic code
                        .build())
                .call()
                .content();
        return bashScript;
    }

    @GetMapping("/openai-explaincodeprompting")
    public String pt_code_prompting_explaining_code(@RequestBody ContextPromptRecord contextPromptRecord) {
        String code = contextPromptRecord.prompt();

        String explanation = chatClient
                .prompt()
                .user(u -> u.text(contextPromptRecord.context()).param("code", code))
                .call()
                .content();
        return explanation;
    }

    @GetMapping("/openai-translatecodeprompting")
    public String pt_code_prompting_translating_code(@RequestBody ContextPromptRecord contextPromptRecord) {
        String bashCode = contextPromptRecord.prompt();

        String pythonCode = chatClient
                .prompt()
                .user(u -> u.text(contextPromptRecord.context()).param("code", bashCode))
                .call()
                .content();
        return pythonCode;
    }

}



