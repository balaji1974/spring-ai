package com.bala.springboot.ai.ai_text2sql.metadata.formatter;

import com.bala.springboot.ai.ai_text2sql.metadata.TableInfo;
import com.bala.springboot.ai.ai_text2sql.util.JsonHelper;

public class JsonTableMetadataFormatter implements TableMetadataFormatter {

  private final JsonHelper jsonHelper;

  public JsonTableMetadataFormatter(JsonHelper jsonHelper) {
    this.jsonHelper = jsonHelper;
  }

  @Override
  public String format(TableInfo tableInfo) {
    return jsonHelper.toJson(tableInfo);
  }
}
