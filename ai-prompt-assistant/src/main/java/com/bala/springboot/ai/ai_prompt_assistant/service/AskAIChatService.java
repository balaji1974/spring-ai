package com.bala.springboot.ai.ai_prompt_assistant.service;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonProperty;

@Service
public class AskAIChatService {
	
	@Autowired
	ChatModel chatModel;
	
	private static final String MODEL="gpt-4o";
	private static final Double TEMPRATURE=0.4D;
	
	public record MathReasoning(
		  @JsonProperty(required = true, value = "steps") Steps steps,
		  @JsonProperty(required = true, value = "final_answer") String finalAnswer) {

		record Steps(
				@JsonProperty(required = true, value = "items") Items[] items) {
	
			record Items(
					@JsonProperty(required = true, value = "explanation") String explanation,
					@JsonProperty(required = true, value = "output") String output) {}
		}
	}
	
	
	public String getResponse(String prompt) {
		return chatModel.call(prompt);
	}
	

	public String getResponseOptions(String prompt) {
		ChatResponse response = chatModel.call(
			    new Prompt(
			        prompt,
			        OpenAiChatOptions.builder()
			            .model(MODEL)
			            .temperature(TEMPRATURE)
			        .build()
			    ));
		return response.getResult().getOutput().getContent();
	}
	
	public MathReasoning getResponeFormatted() {
		var outputConverter = new BeanOutputConverter<>(MathReasoning.class);

		var jsonSchema = outputConverter.getJsonSchema();

		Prompt prompt = new Prompt("how can I solve 8x + 7 = -23",
		  OpenAiChatOptions.builder()
		      .model(MODEL)
		      .temperature(TEMPRATURE)
		      .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
		      .build());

		ChatResponse response = chatModel.call(prompt);
		String content = response.getResult().getOutput().getContent();

		MathReasoning mathReasoning = outputConverter.convert(content);
		return mathReasoning;
	}
	
	
	
}
