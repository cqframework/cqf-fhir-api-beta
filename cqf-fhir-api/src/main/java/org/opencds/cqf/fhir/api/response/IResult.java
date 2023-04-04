package org.opencds.cqf.fhir.api.response;

import java.util.function.Function;
import java.util.function.Supplier;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.api.header.Headers;
import org.opencds.cqf.fhir.api.status.HttpStatus;

public interface IResult<E extends IBaseOperationOutcome, T> {

  boolean hasValue();

  T getOrThrow() throws NullPointerException;

  T getOr(T defaultValue);

  T getOr(Supplier<T> defaultValue);

  <X extends Exception> T getOrThrow(Supplier<X> exception) throws X;

  boolean hasError();

  E getError();

  Headers headers();

  HttpStatus status();

  <R> IResult<E, R> flatMap(Function<? super T, IResult<E, R>> flatMap);

  <R> R flatMap(Function<? super T, IResult<E, R>> flatMap,
      Function<? super Exception, IResult<E, R>> recover);


  <R extends IBaseResource> R map(Function<? super T, R> map);

  <R extends IBaseResource> R map(Function<? super T, R> map,
      Function<? super Exception, R> recover);
}
