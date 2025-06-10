package com.bala.springboot.ai.ai_chat_ollama_huggingface.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_chat_ollama_huggingface.service.ChatbotService;
import com.bala.springboot.ai.ai_chat_ollama_huggingface.dto.ChatRequest;
import com.bala.springboot.ai.ai_chat_ollama_huggingface.dto.ChatResponse;

@RestController
public class ChatController {
	
	private ChatbotService chatbotService;
	
	public ChatController(ChatbotService chatbotService) {
		this.chatbotService=chatbotService;
	}

	@PostMapping("/chat")
	public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest chatRequest) {
	    ChatResponse chatResponse = chatbotService.chat(chatRequest);
	    return ResponseEntity.ok(chatResponse);
	}

}
