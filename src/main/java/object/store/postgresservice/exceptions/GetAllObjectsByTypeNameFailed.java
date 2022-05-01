package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class GetAllObjectsByTypeNameFailed extends InternalServerError {

  public GetAllObjectsByTypeNameFailed(String type) {
    super("Could not get all objects for type: " + type);
  }
}
