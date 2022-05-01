package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class OperationNotSupported extends InternalServerError {

  public OperationNotSupported(String operation) {
    super("Operation no supported: " + operation);
  }
}
