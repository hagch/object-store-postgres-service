package object.store.postgresservice.builders.sql.statement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Objects;
import org.apache.logging.log4j.util.Strings;

public class SQLInsertObjectBuilder {

  private final HashMap<String, String> keyValues;
  private String tableName;

  SQLInsertObjectBuilder() {
    tableName = Strings.EMPTY;
    keyValues = new HashMap<>();
  }

  public SQLInsertObjectBuilder name(String tableName) {
    this.tableName = this.tableName.concat("INSERT INTO " + Strings.dquote(tableName) + "(");
    return this;
  }

  public SQLInsertObjectBuilder keyValue(String key, Object value) {
    keyValues.put(key, Strings.quote(Objects.toString(value)));
    return this;
  }

  public SQLInsertObjectBuilder jsonValue(String key, Object value) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    keyValues.put(key, Strings.quote(mapper.writeValueAsString(value)));
    return this;
  }

  public SQLStatement build() {
    String sqlTableStatement = tableName;
    String columns = String.join(",", keyValues.keySet());
    String values = String.join(",", keyValues.values());
    return new SQLStatement(sqlTableStatement + columns + ") VALUES(" + values + ");");
  }
}
