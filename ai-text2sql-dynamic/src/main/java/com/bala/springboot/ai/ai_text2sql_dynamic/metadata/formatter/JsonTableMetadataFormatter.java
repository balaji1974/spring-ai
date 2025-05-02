package com.bala.springboot.ai.ai_text2sql_dynamic.metadata.formatter;

import com.bala.springboot.ai.ai_text2sql_dynamic.metadata.TableInfo;
import com.bala.springboot.ai.ai_text2sql_dynamic.util.JsonHelper;

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
