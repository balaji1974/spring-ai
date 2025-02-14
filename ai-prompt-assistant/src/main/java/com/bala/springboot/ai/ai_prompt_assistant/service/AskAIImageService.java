package com.bala.springboot.ai.ai_prompt_assistant.service;

import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageModel;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AskAIImageService {
	
	@Autowired
	OpenAiImageModel openAiImageModel;
	
	private static final String MODEL="dall-e-3";

	public ImageResponse generateImage(String prompt) {
		
		ImageResponse response = openAiImageModel.call(
	        new ImagePrompt(prompt,
	        OpenAiImageOptions.builder()
	        	.model(MODEL)
                .quality("hd")
                .N(1)
                .height(1024)
                .width(1024).build())

		);
		return response;
	}
}
