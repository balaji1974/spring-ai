package com.bala.springboot.ai.ai_inject_custom_function.service;

import java.util.function.Function;

import com.bala.springboot.ai.ai_inject_custom_function.util.Request;
import com.bala.springboot.ai.ai_inject_custom_function.util.Response;



public class RectangleAreaService implements Function<Request, Response> {

	@Override
	public Response apply(Request r) {
		return new Response(r.base()*r.height());
	}
}