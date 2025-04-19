package com.bala.springboot.ai.ai_rag_ollama_pdfreader.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_rag_ollama_pdfreader.model.ChatResponse;
import com.bala.springboot.ai.ai_rag_ollama_pdfreader.model.QueryRequest;




@RestController
public class PdfController {

	private final ChatClient chatClient;
	
	private final VectorStore vectorStore;

	private String template;

	private static final Logger log=LoggerFactory.getLogger(PdfController.class);

	public PdfController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
		this.vectorStore=vectorStore;
		this.chatClient = chatClientBuilder.build();
	}


	@PostMapping("/chat")
	public ChatResponse rag(@RequestBody QueryRequest request) {
		
		template = """
				Answer the questions only using the information in the provided knowledge base.
				If you do not know the answer, please response with "I don't know."

				KNOWLEDGE BASE
				---
				{documents}
				""";
		
		log.info("Received request ..........");
		request.setConversationId(UUID.randomUUID().toString());
		// Retrieval
		String relevantDocs = vectorStore.similaritySearch(request.getQuery()).stream().map(Document::getText)
				.collect(Collectors.joining());

		// Augmented
		Message systemMessage = new SystemPromptTemplate(template).createMessage(Map.of("documents", relevantDocs));

		// Generation
		Message userMessage = new UserMessage(request.getQuery());
		Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
		ChatClient.CallResponseSpec res = chatClient.prompt(prompt).call();
		
		ChatResponse chatResponse = new ChatResponse();
		chatResponse.setResponse(res.content());
		chatResponse.setResponseId(request.getConversationId().toString());
		log.info("Response send ..........");
		return chatResponse;
	}

}
