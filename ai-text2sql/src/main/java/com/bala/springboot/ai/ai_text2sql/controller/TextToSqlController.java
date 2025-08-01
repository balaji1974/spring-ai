package com.bala.springboot.ai.ai_text2sql.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_text2sql.util.TextToSqlRequest;
import com.bala.springboot.ai.ai_text2sql.util.TextToSqlResponse;

@RestController
@RequestMapping("/texttosql")
public class TextToSqlController {

  private final ChatClient chatClient;

  public TextToSqlController(ChatClient.Builder builder) {
    this.chatClient = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
  }

  @PostMapping
  public TextToSqlResponse textToSql(@RequestBody TextToSqlRequest request) {
    String sql = chatClient.prompt()
        .user(userSpec -> userSpec.text(request.context())
            .param("user_input", request.userPrompt()))
        .call()
        .content();
    return new TextToSqlResponse(sql);
  }
}
