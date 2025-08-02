package com.bala.springboot.ai.ai_text2sql.util;

import java.util.List;
import java.util.Map;

public record TextToSqlDynamicResponse(String sqlQuery, List<Map<String, Object>> results) { }

