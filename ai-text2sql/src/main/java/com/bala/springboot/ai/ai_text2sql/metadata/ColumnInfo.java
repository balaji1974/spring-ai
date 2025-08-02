package com.bala.springboot.ai.ai_text2sql.metadata;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(Include.NON_NULL)
public record ColumnInfo(String name,
                         String dataType,
                         String description,
                         List<String> possibleValues) {

}
