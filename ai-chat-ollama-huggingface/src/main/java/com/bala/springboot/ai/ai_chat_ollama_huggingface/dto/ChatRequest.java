package com.bala.springboot.ai.ai_chat_ollama_huggingface.dto;

import java.util.UUID;

import org.springframework.lang.Nullable;

public record ChatRequest(@Nullable UUID chatId, String question) {}