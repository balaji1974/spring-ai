package com.bala.springbook.ai.ai_rag_ollama_pdfreader.config;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
public class VectorBuilder {
	private static final Logger log=LoggerFactory.getLogger(VectorBuilder.class);
	
	@Value("${file.path}")
	private Resource resource;
	
	@Bean
	public VectorStore buildVectorStore(EmbeddingModel embeddingModel) {
		SimpleVectorStore vectorStore = SimpleVectorStore.builder(embeddingModel).build();

		String text = null;
		try (PDDocument document = Loader.loadPDF(resource.getFile())) {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			text = pdfStripper.getText(document);
			log.info(text);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		log.info("resource loading start...............");

		Document document = new Document(text);
		List<Document> documentList = List.of(document);

		vectorStore.accept(documentList);
		log.info("resource loading done..............."); 
		
		return vectorStore;
	}

}
