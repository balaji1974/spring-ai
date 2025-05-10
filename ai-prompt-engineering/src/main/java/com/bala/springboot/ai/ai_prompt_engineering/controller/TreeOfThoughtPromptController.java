package com.bala.springboot.ai.ai_prompt_engineering.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class TreeOfThoughtPromptController {
	
	private final ChatClient chatClient;
	
	public TreeOfThoughtPromptController(@Qualifier("anthropicChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }
	
	record Gamemove(String initialmove, String bestMove, String gateProjection) {
	}
    
    
    @GetMapping("/openai-treeofthoughtprompting")
    public Gamemove pt_tree_of_thoughts_game() {
        // Step 1: Generate multiple initial moves
        String initialMoves = chatClient
                .prompt("""
                        You are playing a game of chess. The board is in the starting position.
                        Generate 3 different possible opening moves. For each move:
                        1. Describe the move in algebraic notation
                        2. Explain the strategic thinking behind this move
                        3. Rate the move's strength from 1-10
                        """)
                .options(ChatOptions.builder()
                        .temperature(0.7)
                        .build())
                .call()
                .content();
        
        // Step 2: Evaluate and select the most promising move
        String bestMove = chatClient
                .prompt()
                .user(u -> u.text("""
                        Analyze these opening moves and select the strongest one:
                        {moves}
                        
                        Explain your reasoning step by step, considering:
                        1. Position control
                        2. Development potential
                        3. Long-term strategic advantage
                        
                        Then select the single best move.
                        """).param("moves", initialMoves))
                .call()
                .content();
        
        // Step 3: Explore future game states from the best move
        String gameProjection = chatClient
                .prompt()
                .user(u -> u.text("""
                        Based on this selected opening move:
                        {best_move}
                        
                        Project the next 3 moves for both players. For each potential branch:
                        1. Describe the move and counter-move
                        2. Evaluate the resulting position
                        3. Identify the most promising continuation
                        
                        Finally, determine the most advantageous sequence of moves.
                        """).param("best_move", bestMove))
                .call()
                .content();
        
        return new Gamemove(initialMoves, bestMove, gameProjection);
    }

}



