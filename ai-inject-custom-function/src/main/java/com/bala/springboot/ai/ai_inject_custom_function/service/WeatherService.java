package com.bala.springboot.ai.ai_inject_custom_function.service;

import java.util.function.Function;

import com.bala.springboot.ai.ai_inject_custom_function.util.Unit;
import com.bala.springboot.ai.ai_inject_custom_function.util.WeatherRequest;
import com.bala.springboot.ai.ai_inject_custom_function.util.WeatherResponse;

public class WeatherService implements Function<WeatherRequest, WeatherResponse> {
	private static final int MAX=40;
	private static final int MIN=30;
	
    public WeatherResponse apply(WeatherRequest request) {
    	// This will be the actual weather service URL that we will call in reality
    	// based on request.question() -> which in reality will be a city or country to find the weather
    	return new WeatherResponse((Math.random() * ((MAX - MIN) + 1)) + MIN, Unit.C);
    }
}


