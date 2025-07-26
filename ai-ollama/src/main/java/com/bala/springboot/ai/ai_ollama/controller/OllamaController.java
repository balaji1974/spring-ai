package com.bala.springboot.ai.ai_ollama.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_ollama.util.UserInput;

@RestController
public class OllamaController {
	
	private ChatModel chatModel;
	
	public OllamaController(ChatModel chatModel) {
		this.chatModel = chatModel;
		//OllamaModel.
	}

	@PostMapping("/solve")
	public String solveEquation(@RequestBody UserInput input) {
		System.out.println("Model :"+input.model());
		Prompt prompt = new Prompt(input.equation(),
		        OllamaOptions.builder()
		            //.model(OllamaModel.LLAMA3_2.getName())
		        	.model(input.model())
		            .build());

		ChatResponse response = chatModel.call(prompt);
		String content = response.getResult().getOutput().getText();
	
		return content;
	
	}
}
