package com.bala.springboot.ai.ai_text2sql_dynamic.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class JsonHelper {

  private final ObjectMapper objectMapper;

  public JsonHelper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public String toJson(Object data) {
    try {
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      return Objects.toString(data);
    }
  }
}
