package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.NotFound;

public class ObjectNotFound extends NotFound {

  public ObjectNotFound(String id, String type) {
    super("Object with id: " + id + " from type :" + type + " not found");
  }
}
