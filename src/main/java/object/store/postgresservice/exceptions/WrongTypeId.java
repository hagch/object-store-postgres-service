package object.store.postgresservice.exceptions;


import object.store.postgresservice.exceptions.http.status.InternalServerError;

public class WrongTypeId extends InternalServerError {

  public WrongTypeId() {
    super("Wrong Type Id in request");
  }
}
