package com.bala.springboot.ai.ai_image_generator.service;

import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageGenerationService {

  @Autowired
  ImageModel imageModel;
  
  public String generateImage(String prompt, String modelName, Integer noOfImages,
		  Integer imageHeight, Integer imageWidth) {
	  
	  /* To Do
	   * Additional validation to check for only valid input parameters 
	   * This can be done later
	   */
	  if(modelName==null) modelName="dall-e-2"; // defaults
	  String quality="hd"; // supported only by dall-e-3
	  if(noOfImages==null) noOfImages=1; // defaults
	  if(imageHeight==null) imageHeight=512; // defaults
	  if(imageWidth==null) imageWidth=512; // defaults
	  
	  OpenAiImageOptions imageOptions=OpenAiImageOptions.builder()
							.model(modelName)
							.N(noOfImages)
							.height(imageHeight)
							.width(imageWidth)
							.build();
	  
	  // supported only by dall-e-3
	  if(modelName=="dall-e-3")
		  imageOptions.setQuality(quality); 
						  
	  ImagePrompt imagePrompt = new ImagePrompt(prompt,imageOptions);
	  ImageResponse imageResponse = imageModel.call(imagePrompt);
	  return imageResponse.getResult().getOutput().getUrl();
  }

}