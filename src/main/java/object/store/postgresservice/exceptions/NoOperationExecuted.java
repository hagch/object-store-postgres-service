package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class NoOperationExecuted extends InternalServerError {

  public NoOperationExecuted() {
    super("No Operations where executed");
  }
}
