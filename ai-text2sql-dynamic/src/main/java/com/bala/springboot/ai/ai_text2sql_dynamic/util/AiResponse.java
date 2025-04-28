package com.bala.springboot.ai.ai_text2sql_dynamic.util;

import java.util.List;
import java.util.Map;

public record AiResponse(String sqlQuery, List<Map<String, Object>> results) { }

