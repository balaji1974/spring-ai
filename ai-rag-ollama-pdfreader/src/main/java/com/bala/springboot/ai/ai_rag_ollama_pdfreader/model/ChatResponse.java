package com.bala.springboot.ai.ai_rag_ollama_pdfreader.model;

public class ChatResponse {

	private String responseId;
	private String response;

	public String getResponseId() {
		return responseId;
	}

	public void setResponseId(String responseId) {
		this.responseId = responseId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}