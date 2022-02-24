package object.store.postgresservice.builders.sql.statement;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.logging.log4j.util.Strings;

public class SQLUpdateObjectBuilder {

  private final Set<String> values;
  private String statement;
  private String primaryKey;
  private String primaryValue;

  SQLUpdateObjectBuilder() {
    statement = Strings.EMPTY;
    values = new HashSet<>();
    primaryKey = Strings.EMPTY;
    primaryValue = Strings.EMPTY;
  }

  public SQLUpdateObjectBuilder name(String tableName) {
    this.statement = this.statement.concat("UPDATE " + Strings.dquote(tableName) + " SET ");
    return this;
  }

  public SQLUpdateObjectBuilder primaryKey(String key, String value) {
    this.primaryKey = Strings.dquote(key);
    this.primaryValue = Strings.quote(value);
    return this;
  }

  public SQLUpdateObjectBuilder values(Map<String, Object> values) {
    this.values.addAll(values.entrySet().stream().map(keySet -> Strings.dquote(keySet.getKey()).concat(" = ")
        .concat(Strings.quote(Objects.toString(keySet.getValue())))).collect(
        Collectors.toSet()));
    return this;
  }

  public SQLStatement build() {
    return new SQLStatement(this.statement.concat(String.join(",", this.values))
        .concat(" WHERE ".concat(primaryKey.concat(" = ".concat(primaryValue).concat(";"))))
    );
  }
}
