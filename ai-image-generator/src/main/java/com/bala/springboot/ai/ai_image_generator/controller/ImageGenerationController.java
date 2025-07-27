package com.bala.springboot.ai.ai_image_generator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_image_generator.service.ImageGenerationService;


@RestController()
@RequestMapping("/image")
public class ImageGenerationController {

  @Autowired
  ImageGenerationService imageService;

  @GetMapping("/generate")
  public ResponseEntity<String> generateImage(@RequestParam String prompt,
		  @RequestParam(defaultValue = "dall-e-2")  String modelName,
		  @RequestParam(defaultValue = "1")  Integer noOfImages,
		  @RequestParam(defaultValue = "512")  Integer imageHeight,
		  @RequestParam(defaultValue = "512")  Integer imageWidth
		  
		  ) {
  	return ResponseEntity.ok(imageService.generateImage(prompt,modelName,noOfImages,
  			imageHeight,imageWidth));
  }
}