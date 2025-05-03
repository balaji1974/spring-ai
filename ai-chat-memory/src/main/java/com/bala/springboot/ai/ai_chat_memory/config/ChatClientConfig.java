package com.bala.springboot.ai.ai_chat_memory.config;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatClientConfig {
	
	private static final int MAX_MESSAGE_SIZE=10;

	@Bean
	public ChatMemory jdbcChatMemory(JdbcTemplate jdbcTemplate) {
		ChatMemoryRepository chatMemoryRepository = JdbcChatMemoryRepository.builder()
				.jdbcTemplate(jdbcTemplate)
				.build();

		return MessageWindowChatMemory.builder()
				.chatMemoryRepository(chatMemoryRepository)
				.maxMessages(MAX_MESSAGE_SIZE)
				.build();
	}

}