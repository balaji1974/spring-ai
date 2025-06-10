package com.bala.springboot.ai.ai_chat_ollama_huggingface.dto;

import java.util.UUID;

public record ChatResponse(UUID chatId, String answer) {}