package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class CreateObjectFailed extends InternalServerError {

  public CreateObjectFailed(String object, String type) {
    super("Could not create Object: " + object + " from type: " + type);
  }
}
