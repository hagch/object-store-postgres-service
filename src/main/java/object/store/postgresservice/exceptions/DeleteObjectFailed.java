package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class DeleteObjectFailed extends InternalServerError {

  public DeleteObjectFailed(String id) {
    super("Could not delete Object with id: " + id);
  }
}
