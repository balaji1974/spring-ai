package com.bala.springboot.ai.ai_vector_db.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_vector_db.util.VectorRequest;
import com.bala.springboot.ai.ai_vector_db.util.VectorResponse;

@RestController
public class MongoVectorSearchController {

	@Autowired
	@Qualifier("mongoVectorStore")
	VectorStore vectorStore;
	
	@GetMapping("/mongo-similar-results")
	public VectorResponse searchDocument(@RequestBody VectorRequest request) {
		List<String> result=new ArrayList<>();
		
		var similarRecords = vectorStore
				.similaritySearch(SearchRequest.builder().topK(request.fetchSize())
				.query(request.promptString())
				.build()
		);
		
		System.out.println(similarRecords);
		
		similarRecords.stream().forEach(s -> {
			result.add(s.getText());
		});
		
		return new VectorResponse(result,similarRecords);
		
		
	}
	
	

}
