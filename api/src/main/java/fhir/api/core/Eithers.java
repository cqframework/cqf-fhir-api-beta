package fhir.api.core;

import java.util.function.Function;

public class Eithers {

  private Eithers() {
    // intentionally empty
  }

  public static <L, R> Either<L, R> forLeft(L left) {
    return new Either<>(left, null);
  }

  public static <L, R> Either<L, R> forRight(R right) {
    return new Either<>(null, right);
  }

  public static <L, R> Function<Either<L, R>, Either<L, R>> identity() {
    return x -> x;
  }

}
