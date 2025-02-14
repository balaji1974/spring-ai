package com.bala.springboot.ai.ai_prompt_assistant.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIChatService;
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIChatService.MathReasoning;
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIImageService;

import jakarta.servlet.http.HttpServletResponse;

@RestController 
public class AskAIController {
	
	@Autowired
	AskAIChatService askAIChatService;
	
	@Autowired
	AskAIImageService askAIImageService;
	
	@GetMapping("ask-ai")
	public String getRespone(@RequestParam String prompt) {
		return askAIChatService.getResponse(prompt);
	}
	
	@GetMapping("ask-ai-options")
	public String getResponeOptions(@RequestParam String prompt) {
		return askAIChatService.getResponseOptions(prompt);
	}
	
	@GetMapping(value="ask-ai-math",produces = MediaType.APPLICATION_JSON_VALUE)
	public MathReasoning getResponeMath() {
		return askAIChatService.getResponeFormatted();
	}
	
	@GetMapping("ask-ai-image-generate")
	public void generateImage(HttpServletResponse httpServletResponse, @RequestParam String prompt) throws IOException {
		ImageResponse imageResponse= askAIImageService.generateImage(prompt);
		String imageURL = imageResponse.getResult().getOutput().getUrl();
		
		// Use this in case you need multiple images from the response 
		// Supported in dall-e-2
		/*
		List<String> imageURLs=imageResponse.getResults().stream()
				.map(result-> result.getOutput().getUrl())
				.toList();
		
		*/
		httpServletResponse.sendRedirect(imageURL);
	}
	
}


