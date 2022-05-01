package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class GetAllTypesFailed extends InternalServerError {

  public GetAllTypesFailed() {
    super("Get all types failed");
  }
}
