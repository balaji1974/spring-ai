package com.bala.springboot.ai.ai_text2sql_dynamic.metadata;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.bala.springboot.ai.ai_text2sql_dynamic.util.JsonHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

@Configuration
public class DatabaseMetadataHelper {

  
  @Autowired
  private JsonHelper jsonHelper;
  
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Autowired
  MetadataConfig metadataConfig;
	
  private Map<String, TableMetadataConfig> tablesConfig;
  
  public DatabaseMetadataHelper() {
    if (metadataConfig != null && metadataConfig.tables() != null) {
      this.tablesConfig = metadataConfig.tables();
    } else {
      this.tablesConfig = new HashMap<>();
    }
  }

  public String extractMetadataJson() throws SQLException {
    var metadata = extractMetadata();
    return jsonHelper.toJson(metadata);
  }

  public DatabaseMetadata extractMetadata() throws SQLException {
    var metadata = jdbcTemplate.getDataSource().getConnection().getMetaData();
    var tablesInfo = new ArrayList<TableInfo>();
    try (var tables = metadata.getTables(null, null, null,
        new String[]{"TABLE"})) {
      while (tables.next()) {
        var tableName = tables.getString("TABLE_NAME");
        var tableDescription = tables.getString("REMARKS");
        var tableCatalog = tables.getString("TABLE_CAT");
        var tableSchema = tables.getString("TABLE_SCHEM");
        var columnsInfo = extractColumnMetadata(metadata, tableName);
        tablesInfo.add(
            new TableInfo(tableName, tableDescription,
                tableCatalog, tableSchema, columnsInfo
            )
        );
      }
    }
    return new DatabaseMetadata("default", tablesInfo);
  }

  private List<ColumnInfo> extractColumnMetadata(DatabaseMetaData metadata,
      String tableName) throws SQLException {
    var columnsInfo = new ArrayList<ColumnInfo>();
    try (var columns = metadata.getColumns(null, null, tableName, null)) {
      while (columns.next()) {
        var columnName = columns.getString("COLUMN_NAME");
        var datatype = columns.getString("TYPE_NAME");
        var columnDescription = columns.getString("REMARKS");
        List<String> possibleValues = null;
        if (isEnumColumn(tableName, columnName)) {
          possibleValues = getDistinctValues(tableName, columnName);
        }
        columnsInfo.add(
            new ColumnInfo(columnName, datatype,
                columnDescription, possibleValues)
        );
      }
    }
    return columnsInfo;
  }

  private List<String> getDistinctValues(String tableName, String columnName) {
	return jdbcTemplate.queryForList("SELECT DISTINCT \"%s\" FROM \"%s\"".formatted(columnName, tableName), String.class);
  }

  private boolean isEnumColumn(String table, String column) {
    return Optional.ofNullable(tablesConfig.get(table))
        .map(config -> {
          var columns = config.enumColumns();
          if (CollectionUtils.isEmpty(columns)) {
            return false;
          }
          return columns.contains(column);
        }).orElse(false);
  }
}
