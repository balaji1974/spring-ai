package com.bala.springboot.ai.ai_rag_textreader.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {
	
	private static final int TOP_K_SIMILAR_RESULTS = 2;
	
	@Value("classpath:/prompts/rag-prompt-template.st")
	private Resource template;
	
	private final ChatClient chatClient;
	
	private final VectorStore vectorStore;
	
	
	// Inject our Vector Store created in our configuration  
	// and also build the chat client 
	public RagController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
    	this.chatClient = chatClientBuilder.build();
    	this.vectorStore = vectorStore;
        
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(value = "message", defaultValue = "How many athletes compete in the Olympic Games Paris 2024") String message) {
    	
    	// Read the vector and get the content of the similar document 
    	List<Document> similarDocument=vectorStore.similaritySearch(SearchRequest.builder().query(message).topK(TOP_K_SIMILAR_RESULTS).build());
    	List<String> contentList=similarDocument.stream().map(Document::getText).toList();
    	
    	// Constructing a prompt from prompt template 
    	PromptTemplate promptTemplate=new PromptTemplate(template);
    	Map<String, Object> promptParams = new HashMap<>();
    	promptParams.put("input", message);
    	promptParams.put("documents", String.join("\n", contentList));
    	Prompt prompt=promptTemplate.create(promptParams);
    	
    	// send the prompt containing our input and similar document to the chat client and return our output 
        return chatClient.prompt(prompt).call().content();
 
    }
}
