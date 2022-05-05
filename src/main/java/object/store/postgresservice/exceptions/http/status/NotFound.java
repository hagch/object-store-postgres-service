package object.store.postgresservice.exceptions.http.status;

public abstract class NotFound extends RuntimeException {

  public NotFound(String message) {
    super(message);
  }
}
