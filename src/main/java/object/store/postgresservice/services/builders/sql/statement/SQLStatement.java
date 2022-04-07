package object.store.postgresservice.services.builders.sql.statement;

public record SQLStatement(String statement) {

  public static SQLCreateTableBuilder createTableBuilder() {
    return new SQLCreateTableBuilder();
  }

  public static SQLInsertObjectBuilder insertObjectBuilder() {
    return new SQLInsertObjectBuilder();
  }

  public static SQLSelectObjectBuilder selectObjectBuilder() {
    return new SQLSelectObjectBuilder();
  }

  public static SQLUpdateObjectBuilder updateObjectBuilder() {
    return new SQLUpdateObjectBuilder();
  }

  public String getStatement() {
    return statement;
  }
}
