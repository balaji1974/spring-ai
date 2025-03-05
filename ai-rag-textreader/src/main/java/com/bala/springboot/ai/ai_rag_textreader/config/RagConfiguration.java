package com.bala.springboot.ai.ai_rag_textreader.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class RagConfiguration {
	
	private static final Logger log=LoggerFactory.getLogger(RagConfiguration.class);
	
	// Vector Store Name
	@Value("vectorstore.json")
    private String vectorStoreName;
	
	// Access to the RAG knowledge base  
	@Value("classpath:/docs/olympic-faq.txt")
	private Resource faq;
	
	
    
    /* 
     * Here we inject the EmbeddingModel to the SimpleVectorStore 
     * Spring AI automatically chooses OpenAI EmbeddingModel 
     * since we have spring-ai-openai-spring-boot-starter to our dependencies 
    */
	@Bean
    public VectorStore simpleVectorStore(EmbeddingModel embeddingModel) throws IOException {
		
		/*
		 * SimpleVectorStore is a simple implementation of the VectorStore interface. 
		 * It also provides methods to save the current state of the vectors 
		 * to a file, and to load vectors from a file.
		 */
    	SimpleVectorStore simpleVectorStore = SimpleVectorStore
    		      .builder(embeddingModel)
    		      .build();
    	File vectorStoreFile = getVectorStoreFile();
        
    	/* 
    	 * Check if the Vector Store exist 
    	 * if exist then load it
    	 * if not create a new Vector Store
    	*/ 
    	if (vectorStoreFile.exists()) {
    		log.info("Vector Store File Exists");
    		
    		// Load the contents of the vector store file 
            simpleVectorStore.load(vectorStoreFile);
        
    	}
        else {
            
        	log.info("Vector Store File Does Not Exist, loading documents");
            
        	// Read the resource file
            TextReader textReader = new TextReader(faq); 
            // Add the file name as custom metadata content into the json file being created
            textReader.getCustomMetadata().put("filename", faq.getFile().getName());
            
            // Read the contents of the file, split it and 
            // and put into a document format for the vector store to consume
            List<Document> documents = textReader.get();
            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);
            
            // Add the split document into our vector store and save it
            simpleVectorStore.add(splitDocuments);
            simpleVectorStore.save(vectorStoreFile);
        
        }
        return simpleVectorStore;
    }

    // This function gives the storage path location of the Vector Store
    private File getVectorStoreFile() {
        Path path = Paths.get("src", "main", "resources", "data");
        String absolutePath = path.toFile().getAbsolutePath() + "/" + vectorStoreName;
        return new File(absolutePath);
    }
    
}
