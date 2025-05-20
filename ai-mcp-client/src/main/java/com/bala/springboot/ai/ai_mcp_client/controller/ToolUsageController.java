package com.bala.springboot.ai.ai_mcp_client.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ToolUsageController {
	
	private final ChatClient chatClient;

	public ToolUsageController(
	        ToolCallbackProvider toolsCallbackProvider,
			ChatClient.Builder chatClientBuilder) {
			
		this.chatClient = chatClientBuilder
			
			// Below can be used if we return List<ToolCallback> from the Tools Server
			.defaultToolCallbacks(toolsCallbackProvider.getToolCallbacks())
			
			// Below can be used if we return ToolCallbackProvider from the Tools Server 
			//.defaultToolCallbacks(toolsCallbackProvider)
			.build();
	}
	
	@GetMapping("/tool1")
    String getAllCourses() {

        PromptTemplate pt = new PromptTemplate("""
                Get the list of all available courses.
                """);
        Prompt p = pt.create();
        return this.chatClient.prompt(p)
                .call()
                .content();
    }
	
	@GetMapping("/tool2")
    String getDetailsOfACourse() {

        PromptTemplate pt = new PromptTemplate("""
                Get me Python course details
                """);
        Prompt p = pt.create();
        return this.chatClient.prompt(p)
                .call()
                .content();
    }

}
