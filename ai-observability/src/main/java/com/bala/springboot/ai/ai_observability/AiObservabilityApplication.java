package com.bala.springboot.ai.ai_observability;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AiObservabilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiObservabilityApplication.class, args);
	}
	
	@Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

}
