package object.store.postgresservice.exceptions;

import object.store.postgresservice.exceptions.http.status.NotFound;

public class TypeNotFoundByName extends NotFound {

  public TypeNotFoundByName(String name) {
    super("Type with name: " + name + " not found");
  }
}
