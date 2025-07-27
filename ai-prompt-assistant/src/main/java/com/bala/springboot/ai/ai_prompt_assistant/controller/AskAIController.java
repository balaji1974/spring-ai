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
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIImageService;
import com.bala.springboot.ai.ai_prompt_assistant.service.AskAIRecipeService;
import com.bala.springboot.ai.ai_prompt_assistant.util.MathReasoning;

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
		return askAIChatService.getChatResponse(prompt);
	}
	
	/*
	 * Send model name and temperature as input parameters to make the output more dynamic
	 * 
	 * Lower Temperature (e.g., closer to 0): This makes the model's output more deterministic 
	 * and focused. The model is more likely to choose the most probable next word or token, 
	 * resulting in less variability and more predictable responses. 
	 * This is suitable for tasks requiring factual accuracy or consistent output.
	 * 
	 * Higher Temperature (e.g., closer to 1): This increases the randomness and creativity 
	 * of the model's output. The model is more likely to explore less probable but still 
	 * plausible options, leading to more diverse and potentially surprising responses. 
	 * This is useful for creative writing, brainstorming, or open-ended conversations. 
	 */
	
	
	@GetMapping("ask-ai-options")
	public String getResponeOptions(
			@RequestParam String prompt,
			@RequestParam(defaultValue ="gpt-4o") String model,
			@RequestParam(defaultValue = "0.4D") Double temprature) {
		return askAIChatService.getChatResponseWithOptions(prompt,model,temprature);
	}
	
	/*
	 * Customized input parameter and formatted JSON output 
	 */
	
	@GetMapping(value="ask-ai-math",produces = MediaType.APPLICATION_JSON_VALUE)
	public MathReasoning getResponeMath(
			@RequestParam String prompt,
			@RequestParam(defaultValue ="gpt-4o") String model,
			@RequestParam(defaultValue = "0.4D") Double temprature) {
		return askAIChatService.getMathResponseFormatted(prompt, model, temprature); 
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
	public String getResponeRecipe(
			@RequestParam String ingredients, 
			@RequestParam(defaultValue = "any") String cuisine, 
			@RequestParam(defaultValue = "") String dietaryRestrictions) {
		return askAIIRecipeService.createRecipe(ingredients, cuisine, dietaryRestrictions);
	}
	
}


