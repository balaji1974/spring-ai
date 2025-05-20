package com.bala.springboot.ai.ai_mcp_server.config;

import java.util.List;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bala.springboot.ai.ai_mcp_server.service.CourseService;

@Configuration
public class ToolsConfig {
	
	
	// First way
	/*
	 * This is using ToolCallbacks.from() method The ToolCallbacks.from() method
	 * scans the service class for @Tool annotations and registers them with the MCP
	 * framework.
	 */
	
	@Bean
	public List<ToolCallback> myTools(CourseService courseService) {
		// The ToolCallbacks.from() method scans the service class for 
		// @Tool annotations and registers them with the MCP framework.
		return List.of(ToolCallbacks.from(courseService));
	}
	
	
	// Second way
	/*
	 * This is using MethodToolCallbackProvider The MethodToolCallbackProvider
	 * scans the service class for @Tool annotations and registers them with the MCP
	 * framework.
	 */
	
	/*
	@Bean
    public ToolCallbackProvider tools(CourseService courseService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(courseService)
                .build();
    }
    */
}
