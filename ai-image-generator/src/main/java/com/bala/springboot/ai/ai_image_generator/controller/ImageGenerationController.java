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
		  @RequestParam(required=false)  String modelName,
		  @RequestParam(required=false)  Integer noOfImages,
		  @RequestParam(required=false)  Integer imageHeight,
		  @RequestParam(required=false)  Integer imageWidth
		  
		  ) {
  	return ResponseEntity.ok(imageService.generateImage(prompt,modelName,noOfImages,
  			imageHeight,imageWidth));
  }
}