package com.bala.springboot.ai.ai_text2sql.metadata;

import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "database-metadata")
public record MetadataConfig(Map<String, TableMetadataConfig> tables) {

}
