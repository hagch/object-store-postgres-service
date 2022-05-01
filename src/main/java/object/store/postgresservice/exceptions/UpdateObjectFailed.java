package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class UpdateObjectFailed extends InternalServerError {

  public UpdateObjectFailed(String object, String type) {
    super("Could not update Object: " + object + " from type: " + type);
  }
}
