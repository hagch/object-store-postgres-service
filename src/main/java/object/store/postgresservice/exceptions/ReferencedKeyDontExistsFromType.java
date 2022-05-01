package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class ReferencedKeyDontExistsFromType extends InternalServerError {

  public ReferencedKeyDontExistsFromType(String key, String type) {
    super("Referenced key: " + key + " dont exists at type: " + type);
  }
}
