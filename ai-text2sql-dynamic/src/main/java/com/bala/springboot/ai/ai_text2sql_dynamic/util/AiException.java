package com.bala.springboot.ai.ai_text2sql_dynamic.util;

public class AiException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public AiException(String response) {
	  super(response);
  }
  
}