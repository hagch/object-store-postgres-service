package object.store.postgresservice.services.builders.sql.statement;

import java.util.HashMap;
import org.apache.logging.log4j.util.Strings;

public class SQLSelectObjectBuilder {

  private final HashMap<String, String> keyValues;
  private String statement;

  SQLSelectObjectBuilder() {
    statement = Strings.EMPTY;
    keyValues = new HashMap<>();
  }

  public SQLSelectObjectBuilder name(String tableName) {
    this.statement = this.statement.concat("SELECT * FROM " + Strings.dquote(tableName));
    return this;
  }

  public SQLSelectObjectBuilder byKey(String key, String value) {
    this.statement = this.statement.concat(" WHERE ").concat(Strings.dquote(key)).concat(" = ")
        .concat(Strings.quote(value));
    return this;
  }

  public SQLStatement build() {
    return new SQLStatement(statement.concat(";"));
  }
}
