package com.bala.springboot.ai.ai_chat_ollama_huggingface.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.bala.springboot.ai.ai_chat_ollama_huggingface.dto.ChatRequest;
import com.bala.springboot.ai.ai_chat_ollama_huggingface.dto.ChatResponse;

@Service
public class ChatbotService {

	private ChatClient chatClient;
	
	public ChatbotService(ChatClient chatClient) {
		this.chatClient=chatClient;
	}

	public ChatResponse chat(ChatRequest chatRequest) {
	    UUID chatId = Optional
	      .ofNullable(chatRequest.chatId())
	      .orElse(UUID.randomUUID());
	    
	    String answer = chatClient
	      .prompt()
	      .user(chatRequest.question())
	      .advisors(advisorSpec ->
	          advisorSpec
	            .param("chat_memory_conversation_id", chatId))
	      .call()
	      .content();
	    return new ChatResponse(chatId, answer);
	}
	
	
}
