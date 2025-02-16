package com.bala.springboot.ai.ai_prompt_assistant.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIChatService;
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIChatService.MathReasoning;
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIImageService;
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIRecipeService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class AskAIController {
	
	@Autowired
	AskAIChatService askAIChatService;
	
	@Autowired
	AskAIImageService askAIImageService;
	
	@Autowired
	AskAIRecipeService askAIIRecipeService;
	
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
	public List<String> generateImage(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, @RequestParam String prompt) throws IOException {
		ImageResponse imageResponse= askAIImageService.generateImage(prompt);
		//String imageURL = imageResponse.getResult().getOutput().getUrl();
		
		// Use this in case you need multiple images from the response 
		// Supported in dall-e-2
		
		List<String> imageURLs=imageResponse.getResults().stream()
				.map(result-> result.getOutput().getUrl())
				.toList();
		
		return imageURLs;
		
		//httpServletResponse.sendRedirect(imageURL);
	}
	
	@GetMapping("ask-ai-recipe-creator")
	public String getResponeRecipe(@RequestParam String ingredients, 
			@RequestParam(defaultValue = "any") String cuisine, 
			@RequestParam(defaultValue = "") String dietaryRestrictions) {
		return askAIIRecipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
	}
	
}


