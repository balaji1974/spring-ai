package com.bala.springboot.ai.ai_vector_db.util;

import java.util.List;

import org.springframework.ai.document.Document;

public record VectorResponse(List<String> results, List<Document> documents) {

}
