package com.bala.springboot.ai.ai_text2sql.metadata.formatter;

import com.bala.springboot.ai.ai_text2sql.metadata.TableInfo;

public interface TableMetadataFormatter {

  String format(TableInfo tableInfo);
}
