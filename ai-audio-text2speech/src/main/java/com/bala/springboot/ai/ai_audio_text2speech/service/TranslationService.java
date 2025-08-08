package com.bala.springboot.ai.ai_audio_text2speech.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;

@Service
public class TranslationService {

	private final ChatClient chatClient;

    public TranslationService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel).build();
    }

    public String translateToArabic(String englishText, String lang) {
    	
        String prompt = "Translate the following English sentence into "+ lang +"\n" + englishText;
        
        
        return this.chatClient.prompt()
        .user(prompt)
        .call()
        .content();
    }
}