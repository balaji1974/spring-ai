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

  private static final String USER_PROMPT_TEMPLATE = """
      You are a Postgres expert. Please generate SQL statements to answer user's query. 
      
      The table name is netflix_shows. Column names and data types are shown as below: 
      show_id, text; type, text; title, text; director, text; cast_members, text; country, text; 
      date_added, date; release_year, int4; rating, text; duration, text; 
      listed_in, text; description, text.
      
      Output the SQL only. Don't use Markdown format and output the query in a single line.
      
      {user_input}
      """;

  public TextToSqlController(ChatClient.Builder builder) {
    this.chatClient = builder.defaultAdvisors(new SimpleLoggerAdvisor()).build();
  }

  @PostMapping
  public TextToSqlResponse textToSql(@RequestBody TextToSqlRequest request) {
    String sql = chatClient.prompt()
        .user(userSpec -> userSpec.text(USER_PROMPT_TEMPLATE)
            .param("user_input", request.input()))
        .call()
        .content();
    return new TextToSqlResponse(sql);
  }
}
