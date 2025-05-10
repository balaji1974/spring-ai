package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIChatController {

    private final ChatClient chatClient;

    public OpenAIChatController(@Qualifier("openAIChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/openai-chatoptions")
    public String openAi() {
    	
    	String myPrompt="how can I solve 8x + 7 = -23";
    	
    	String jsonSchema = """
    			  {
    			      "type": "object",
    			      "properties": {
    			          "steps": {
    			              "type": "array",
    			              "items": {
    			                  "type": "object",
    			                  "properties": {
    			                      "explanation": { "type": "string" },
    			                      "output": { "type": "string" }
    			                  },
    			                  "required": ["explanation", "output"],
    			                  "additionalProperties": false
    			              }
    			          },
    			          "final_answer": { "type": "string" }
    			      },
    			      "required": ["steps", "final_answer"],
    			      "additionalProperties": false
    			  }
    			  """;
    	OpenAiChatOptions openAiOptions = OpenAiChatOptions.builder()
    			
    			/*
    			 * OpenAI model to use
    			 */
    	        .model("gpt-4o") 
    	        
    	        /* 
    	         * Temperature controls the randomness or "creativity" of the model's response. 
    	         * Lower values (0.0-0.3): More deterministic, focused responses. 
    	         * Better for factual questions, classification, or tasks where consistency is critical.
    	         * Medium values (0.4-0.7): Balanced between determinism and creativity. Good for general use cases. 
    	         * Higher values (0.8-1.0): More creative, varied, and potentially surprising responses. 
    	         * Better for creative writing, brainstorming, or generating diverse options.
    	         */
    	        .temperature(0.2) 			// OpenAI 
    	        
    	        /*
    	         * This parameter is used to discourage the model from repeating the same words
    	         * or phrases too frequently within the generated text. A higher frequency_penalty value will 
    	         * result in the model being more conservative in its use of repeated tokens.
    	         */
    	        .frequencyPenalty(0.5)      // OpenAI-specific parameter
    	        
    	        /*
    	         * This parameter is used to encourage the model to include a diverse range of tokens in the 
    	         * generated text. A higher presence_penalty value will result in the model being more likely 
    	         * to generate tokens that have not yet been included in the generated text. 
    	         */
    	        .presencePenalty(0.3)       // OpenAI-specific parameter
    	        
    	        //.responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, "json_object"))  // OpenAI-specific JSON mode
    	        
    	        /*
    	         * limits how many tokens (word pieces) the model can generate in its response.
    	         * Low values (5-25): For single words, short phrases, or classification labels. 
    	         * Medium values (50-500): For paragraphs or short explanations.
    	         * High values (1000+): For long-form content, stories, or complex explanations.
    	         */
    	        .maxTokens(250) 
    	        
    	        /*
    	         * Top-K: Limits token selection to the K most likely next tokens. 
    	         * Higher values (e.g., 40-50) introduce more diversity.
    	         */
    	        .topP(40d)      // Consider only the top 40 tokens
    	        
    	        /*
    	         * Top-P (nucleus sampling): Dynamically selects from the smallest set of tokens
    	         *  whose cumulative probability exceeds P. Values like 0.8-0.95 are common.
    	         */
    	        .topP(0.8)     // Sample from tokens that cover 80% of probability mass
    	        
    	        /*
    	         * If specified, model will make a best effort to sample deterministically, such that 
    	         * repeated requests with the same seed and parameters should return the same result.
    	         */
    	        .seed(42)      // OpenAI-specific deterministic generation
    	        
    	        /*
    	         * Structured Outputs feature guarantees the AI model will generate responses that conform to a supplied JSON Schema. 
    	         * This addresses several common challenges in AI-powered applications: 
    	         * Type Safety: No more worrying about missing required keys or invalid enum values; 
    	         * Explicit Refusals: Safety-based model refusals become programmatically detectable; 
    	         * Simplified Prompting: Achieve consistent formatting without resorting to overly specific prompts.
    	         */
    	        .responseFormat(new ResponseFormat(ResponseFormat.Type.JSON_SCHEMA, jsonSchema))
    	        .build();

    	
        return chatClient.prompt()
        		.options(openAiOptions)
                .user(myPrompt)
                .call()
                .content();
    }
}