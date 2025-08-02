package com.bala.springboot.ai.ai_text2sql.metadata;

import java.util.List;

public record TableInfo(String name,
                        String description,
                        String catalog,
                        String schema,
                        List<ColumnInfo> columns) {

  public TableInfo withDescription(String description) {
    return new TableInfo(name, description, catalog, schema, columns);
  }
}
