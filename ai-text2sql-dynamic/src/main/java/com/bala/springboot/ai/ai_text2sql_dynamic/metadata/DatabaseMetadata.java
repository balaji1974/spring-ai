package com.bala.springboot.ai.ai_text2sql_dynamic.metadata;

import java.util.List;

public record DatabaseMetadata(String database, List<TableInfo> tables) {

}
