package com.bala.springboot.ai.ai_inject_custom_function.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.bala.springboot.ai.ai_inject_custom_function.service.RectangleAreaService;
import com.bala.springboot.ai.ai_inject_custom_function.util.Request;
import com.bala.springboot.ai.ai_inject_custom_function.util.Response;

@Configuration
public class CustomFunctions {
	
	@Bean
	@Description("Calculate the area of a rectangle from its base and height")
	public Function<Request, Response> rectangeleAreaFunction() {
		return new RectangleAreaService();
	}

}