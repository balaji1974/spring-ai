package com.bala.springboot.ai.ai_inject_custom_function.config;

import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

import com.bala.springboot.ai.ai_inject_custom_function.service.WeatherService;
import com.bala.springboot.ai.ai_inject_custom_function.util.WeatherRequest;
import com.bala.springboot.ai.ai_inject_custom_function.util.WeatherResponse;

@Configuration(proxyBeanMethods = false)
class WeatherTools {

    WeatherService weatherService = new WeatherService();

	@Bean
	@Description("Get the weather in location")
	Function<WeatherRequest, WeatherResponse> currentWeather() {
		return weatherService;
	}

}