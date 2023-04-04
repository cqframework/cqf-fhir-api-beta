package org.opencds.cqf.fhir.api.error;

public class Errors {

  private Errors() {
    // intentionally empty
  }

  public static Error forException(Throwable exception) {
    return new Error(exception, null);
  }

  public static Error forMessage(String message) {
    return new Error(null, message);
  }

}
