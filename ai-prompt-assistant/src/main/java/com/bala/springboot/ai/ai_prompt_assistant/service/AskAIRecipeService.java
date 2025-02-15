package com.bala.springboot.ai.ai_prompt_assistant.service;

import java.util.Map;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AskAIRecipeService {
	@Autowired
	ChatModel chatModel;
	
	public String createRecipe(String ingredients, String cuisine, String dietaryRestrictions ) {
		var template ="""
					I want to create a recipie using the following ingredients: {ingredients}.
					The cuisine type I prefer is: {cuisine}.
					Please consider the following dietaryRestrictions: {dietaryRestrictions}.
					Please provide me a detail recipe including title, list of ingredients, and cooking instructions
				""";
		
		PromptTemplate promptTemplate=new PromptTemplate(template);
		Map<String, Object> params=Map.of(
				"ingredients", ingredients,
				"cuisine", cuisine,
				"dietaryRestrictions", dietaryRestrictions
				);
		Prompt prompt=promptTemplate.create(params);
		return chatModel.call(prompt).getResult().getOutput().getContent();
		
	}

}
