package com.bala.springboot.ai.ai_text2sql.util;

public class TextToSqllDynamicException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TextToSqllDynamicException(String response) {
	  super(response);
  }
  
}