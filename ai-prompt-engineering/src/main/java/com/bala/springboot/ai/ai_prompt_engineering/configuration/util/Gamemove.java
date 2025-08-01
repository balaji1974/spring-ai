package com.bala.springboot.ai.ai_prompt_engineering.configuration.util;

public record Gamemove(String initialmove, String bestMove, String gateProjection) {
	
	
	@Override
	  public String toString() {
		String regex = "\\*"; 
	    return 
	    	initialmove.replaceAll(regex, "")
	    	
	    	+
	    	bestMove.replaceAll(regex, "")
	    	
	    	+ 
	    	gateProjection.replaceAll(regex, "")
	    	
	    	;
	  }
}