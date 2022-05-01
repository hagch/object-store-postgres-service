package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.NotFound;

public class TypeNotFoundById extends NotFound {

  public TypeNotFoundById(String typeId) {
    super("Type with id: " + typeId + " not found");
  }
}
