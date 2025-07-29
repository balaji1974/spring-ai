package com.bala.springboot.ai.ai_inject_custom_function.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_inject_custom_function.service.WeatherService;
import com.bala.springboot.ai.ai_inject_custom_function.util.WeatherRequest;

@RestController
public class AIController {
	
	private final ChatModel chatModel;
	
	public AIController(ChatModel chatModel) {
		super();
		this.chatModel = chatModel;
	}

	@GetMapping("/area-rectangle")
	public Generation areaOfRectangle(@RequestParam(value = "message") String message) {
		Prompt prompt = new Prompt(message,
				OpenAiChatOptions.builder()
				.function("rectangeleAreaFunction")
				.build());
		ChatResponse response = chatModel.call(prompt);
		return response.getResult();
	}
	
	@PostMapping("/weather")
	public String getTemperature(@RequestBody WeatherRequest weatherRequest) {
		ToolCallback toolCallback = FunctionToolCallback
			    .builder("currentWeather", new WeatherService())
			    .description("Get the weather in location")
			    .inputType(WeatherRequest.class)
			    .build();
		
		return ChatClient.create(chatModel)
	    .prompt(weatherRequest.question())
	    .tools(toolCallback)
	    .call()
	    .content().concat("\n\n**Note: This is a random generated weather from our internal tools");

	}
	
	@PostMapping("/weather-tools")
	public String getTemperatureWithTools(@RequestBody WeatherRequest weatherRequest) {
		return ChatClient.create(chatModel)
	    .prompt(weatherRequest.question())
	    .tools("currentWeather")
	    .call()
	    .content().concat("\n\n**Note: This is a random generated weather from our internal tools");

	}

	

}
