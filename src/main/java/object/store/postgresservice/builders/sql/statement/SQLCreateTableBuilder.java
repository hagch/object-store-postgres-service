package object.store.postgresservice.builders.sql.statement;

import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;
import org.everit.json.schema.Schema;

public class SQLCreateTableBuilder {

  private final static String COMMA_DELIMITER = ",";
  private final static String SPACE_DELIMITER = " ";
  private final TreeSet<String> items;
  private final TreeSet<String> constraints;
  private String tableName;
  private String primaryKey;

  SQLCreateTableBuilder() {
    primaryKey = Strings.EMPTY;
    tableName = Strings.EMPTY;
    items = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    constraints = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
  }

  public SQLCreateTableBuilder name(String tableName) {
    this.tableName = this.tableName.concat("CREATE TABLE IF NOT EXISTS " + Strings.dquote(tableName) + "(");
    return this;
  }

  public SQLCreateTableBuilder fieldBoolean(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "BOOLEAN"));
    return this;
  }

  public SQLCreateTableBuilder fieldUuid(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "UUID"));
    return this;
  }

  public SQLCreateTableBuilder fieldInteger(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "INTEGER"));
    return this;
  }

  public SQLCreateTableBuilder fieldLong(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "BIGINT"));
    return this;
  }

  public SQLCreateTableBuilder fieldDouble(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "NUMERIC"));
    return this;
  }

  public SQLCreateTableBuilder fieldString(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "TEXT"));
    return this;
  }

  public SQLCreateTableBuilder fieldTimeStamp(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "TIMESTAMP"));
    return this;
  }

  public SQLCreateTableBuilder fieldObject(String key, boolean isNullAble) {
    items.add(field(key, isNullAble, "JSONB"));
    return this;
  }

  public SQLCreateTableBuilder primaryKey(String... keys) {
    String key = Arrays.stream(keys).map(Strings::dquote).collect(Collectors.joining(COMMA_DELIMITER));
    primaryKey = "PRIMARY KEY (" + key + ")";
    return this;
  }

  public SQLCreateTableBuilder foreignKey(String name, String column, String fkTable, String fkColumn) {
    constraints.add("CONSTRAINT " + Strings.dquote(name) + " FOREIGN KEY (" + Strings.dquote(column) + ") REFERENCES"
        + SPACE_DELIMITER + Strings.dquote(fkTable) + "(" + Strings.dquote(fkColumn) + ")");
    return this;
  }

  public SQLCreateTableBuilder jsonValidation(String name, String column, Schema schema) {
    constraints.add("CONSTRAINT " + Strings.dquote(name) + " CHECK (validate_json_schema(" +
        Strings.quote(schema.toString()) + ", " + Strings.dquote(column) + "))");
    return this;
  }

  public SQLStatement build() {
    String sqlTableStatement = tableName;
    sqlTableStatement = sqlTableStatement.concat(String.join(COMMA_DELIMITER, items));
    if (!sqlTableStatement.endsWith(COMMA_DELIMITER)) {
      sqlTableStatement = sqlTableStatement.concat(COMMA_DELIMITER);
    }
    sqlTableStatement = sqlTableStatement.concat(String.join(COMMA_DELIMITER, primaryKey));
    if (!sqlTableStatement.endsWith(COMMA_DELIMITER) && !constraints.isEmpty()) {
      sqlTableStatement = sqlTableStatement.concat(COMMA_DELIMITER);
    }
    sqlTableStatement = sqlTableStatement.concat(String.join(COMMA_DELIMITER, constraints));
    return new SQLStatement(sqlTableStatement.concat(");"));
  }

  private String field(String key, boolean isNullAble, String type) {
    String field = Strings.dquote(key).concat(SPACE_DELIMITER).concat(type);
    if (!isNullAble) {
      field = field.concat(SPACE_DELIMITER).concat("NOT NULL");
    }
    return field;
  }
}
