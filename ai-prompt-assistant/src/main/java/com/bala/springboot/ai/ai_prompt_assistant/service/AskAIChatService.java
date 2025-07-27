package com.bala.springboot.ai.ai_prompt_assistant.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bala.springboot.ai.ai_prompt_assistant.util.MathReasoning;

@Service
public class AskAIChatService {
	
	@Autowired
	ChatModel chatModel;
	
	
	
	public String getChatResponse(String prompt) {
		return chatModel.call(prompt);
	}
	
	public String getChatResponseWithOptions(String prompt, String model, Double temprature) {
		ChatResponse response = chatModel.call(
			    new Prompt(
			        prompt,
			        OpenAiChatOptions.builder()
			            .model(model)
			            .temperature(temprature)
			        .build()
			    ));
		return response.getResult().getOutput().getContent();
	}
	
	public MathReasoning getMathResponseFormatted(String question, String model, Double temprature) {
		var outputConverter = new BeanOutputConverter<>(MathReasoning.class);

		var jsonSchema = outputConverter.getJsonSchema();

		Prompt prompt = new Prompt(question,
		  OpenAiChatOptions.builder()
		      .model(model)
		      .temperature(temprature)
		      .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
		      .build());

		ChatResponse response = chatModel.call(prompt);
		String content = response.getResult().getOutput().getContent();
		
		MathReasoning mathReasoning = outputConverter.convert(content);
		return mathReasoning;
	}
	
	
	
}
