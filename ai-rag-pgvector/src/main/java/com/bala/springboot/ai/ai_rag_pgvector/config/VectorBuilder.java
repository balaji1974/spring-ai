package com.bala.springboot.ai.ai_rag_pgvector.config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.JdbcTemplate;

import groovy.util.logging.Slf4j;


@Configuration
@Slf4j
public class VectorBuilder {
	
	private static final Logger log = LoggerFactory.getLogger(VectorBuilder.class);
	
	@Autowired
	private ResourceLoader resourceLoader;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	VectorStore vectorStore;
	
	@Value("${documents.directory.path}") 
	String directoryPath;

	
	@Bean
	public VectorStore buildVectorStore() throws IOException {
		
		Resource directoryResource = resourceLoader.getResource(directoryPath);
		log.info("Reading files from directory : "+ directoryResource.getURL().getPath());
		Path directory = Paths.get(directoryResource.getURL().getPath());
		try(var paths = Files.list(directory)){
			paths.filter(Files::isRegularFile)
					.forEach(path -> {
                        try {
							String fileName = path.getFileName().toString();
							String sql = "SELECT count(*) FROM vector_store WHERE metadata->>'file_name' = ?";
							Integer count = jdbcTemplate.queryForObject(sql, Integer.class, new Object[]{fileName});
							if(count == 0) {
								Resource resource = new UrlResource(path.toUri());
								log.info("Reading PDF: {}", resource.getFilename());
								int startTime = (int) System.currentTimeMillis();
								
								
								PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
										PdfDocumentReaderConfig.builder()
												.withPageTopMargin(0)
												.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
														.withNumberOfTopTextLinesToDelete(0)
														.build())
												.withPagesPerDocument(1)
												.build());

								var textSplitter = new TokenTextSplitter();
								List<Document> docs = textSplitter.apply(pdfReader.get());
								log.info("Loading PDF: {}", resource.getFilename());
								vectorStore.accept(docs);
								log.info("Loaded PDF complete: {}", resource.getFilename());
								int endTime = (int) System.currentTimeMillis();
								log.info("Time taken to load PDF: {} ms", endTime - startTime);
							}

						} 
                        catch (MalformedURLException e) {
							throw new RuntimeException(e);
						}
					});
		}
		return vectorStore;
	}

}
