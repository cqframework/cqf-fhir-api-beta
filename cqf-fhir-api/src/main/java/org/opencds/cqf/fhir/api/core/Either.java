package org.opencds.cqf.fhir.api.core;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.function.Consumer;
import java.util.function.Function;


public class Either<L, R> {
  private final L left;
  private final R right;

  protected Either(L left, R right) {
    checkArgument(left == null ^ right == null, "left and right are mutually exclusive");
    this.left = left;
    this.right = right;
  }

  public L left() {
    checkState(isLeft());
    return left;
  }

  public R right() {
    checkState(isRight());
    return right;
  }

  public boolean isLeft() {
    return left != null;
  }

  public boolean isRight() {
    return right != null;
  }

  public void forEach(
      Consumer<? super R> forEach) {
    checkNotNull(forEach);
    forEach.accept(right());
  }

  public <T> T map(
      Function<? super R, ? extends T> map) {
    checkNotNull(map);
    return map.apply(right());
  }

  public <T> Either<L, T> flatMap(
      Function<? super R, Either<L, T>> flatMap) {
    checkNotNull(flatMap);
    return flatMap(Eithers::forLeft, flatMap);
  }

  public <T> Either<L, T> flatMap(
      Function<? super L, Either<L, T>> recovery,
      Function<? super R, Either<L, T>> flatMap) {
    checkNotNull(recovery);
    checkNotNull(flatMap);
    if (isRight()) {
      return flatMap.apply(right());
    }

    return recovery.apply(left());
  }

  public Either<R, L> swap() {
    if (isLeft()) {
      return Eithers.forRight(left());
    }

    return Eithers.forLeft(right());
  }


  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Either<?, ?>) {
      Either<?, ?> other = (Either<?, ?>) obj;
      return (this.left == other.left && this.right == other.right)
          || (this.left != null && other.left != null && this.left.equals(other.left))
          || (this.right != null && other.right != null && this.right.equals(other.right));
    }

    return false;
  }

  @Override
  public int hashCode() {
    if (this.left != null) {
      return this.left.hashCode();
    }

    return this.right.hashCode();
  }
}
