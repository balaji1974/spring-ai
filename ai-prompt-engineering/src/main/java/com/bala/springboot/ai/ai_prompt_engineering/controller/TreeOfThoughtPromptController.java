package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.Gamemove;
import com.bala.springboot.ai.ai_prompt_engineering.configuration.util.TreeOfThoughtPromptRecord;


@RestController
public class TreeOfThoughtPromptController {
	
	private final ChatClient chatClient;
	
	public TreeOfThoughtPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
	
	
    @GetMapping("/openai-treeofthoughtprompting")
    public String pt_tree_of_thoughts_game(@RequestBody TreeOfThoughtPromptRecord promptAndContext) {
        // Step 1: Generate multiple initial moves
        String initialMoves = chatClient
                .prompt(promptAndContext.prompt())
                .options(ChatOptions.builder()
                        .temperature(0.7)
                        .build())
                .call()
                .content();
        
        // Step 2: Evaluate and select the most promising move
        String bestMove = chatClient
                .prompt()
                .user(u -> u.text(promptAndContext.context1()).param("moves", initialMoves))
                .call()
                .content();
        
        // Step 3: Explore future game states from the best move
        String gameProjection = chatClient
                .prompt()
                .user(u -> u.text(promptAndContext.context2()).param("best_move", bestMove))
                .call()
                .content();
        
        return new Gamemove(initialMoves, bestMove, gameProjection).toString();
    }

}



