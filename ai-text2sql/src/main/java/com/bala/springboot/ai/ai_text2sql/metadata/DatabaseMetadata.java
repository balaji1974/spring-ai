package com.bala.springboot.ai.ai_text2sql.metadata;

import java.util.List;

public record DatabaseMetadata(String database, List<TableInfo> tables) {

}
