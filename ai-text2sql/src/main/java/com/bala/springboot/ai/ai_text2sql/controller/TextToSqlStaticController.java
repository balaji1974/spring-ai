package com.bala.springboot.ai.ai_text2sql.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bala.springboot.ai.ai_text2sql.util.TextToSqStaticlRequest;
import com.bala.springboot.ai.ai_text2sql.util.TextToSqlStaticResponse;

@RestController
@RequestMapping("/texttosql-static")
public class TextToSqlStaticController {

  private final ChatClient chatClient;

  public TextToSqlStaticController(ChatClient.Builder builder) {
    this.chatClient = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
  }

  @PostMapping
  public TextToSqlStaticResponse textToSql(@RequestBody TextToSqStaticlRequest request) {
    String sql = chatClient.prompt()
        .user(userSpec -> userSpec.text(request.context())
            .param("user_input", request.userPrompt()))
        .call()
        .content();
    return new TextToSqlStaticResponse(sql);
  }
}
