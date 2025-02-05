package com.bala.springboot.ai.ai_image_generator.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageGenerationService {

  @Autowired
  ImageModel imageModel;
  
  public String generateImage(String prompt) {
	  ImagePrompt imagePrompt = new ImagePrompt(prompt);
	  ImageResponse imageResponse = imageModel.call(imagePrompt);
	  return imageResponse.getResult().getOutput().getUrl();
  }

}