package fhir.api.error;

import fhir.api.core.Either;

// TO DISCUSS: Operation Outcome instead of a String? All three?
public class Error extends Either<Throwable, String> {

  protected Error(Throwable exception, String message) {
    super(exception, message);
  }

  public Throwable exception() {
    return this.left();
  }

  public String message() {
    return this.right();
  }

  public boolean isException() {
    return this.isLeft();
  }

  public boolean isMessage() {
    return this.isRight();
  }
}
