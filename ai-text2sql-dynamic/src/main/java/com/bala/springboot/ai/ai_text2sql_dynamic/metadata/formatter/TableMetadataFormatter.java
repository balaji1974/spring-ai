package com.bala.springboot.ai.ai_text2sql_dynamic.metadata.formatter;

import com.bala.springboot.ai.ai_text2sql_dynamic.metadata.TableInfo;

public interface TableMetadataFormatter {

  String format(TableInfo tableInfo);
}
