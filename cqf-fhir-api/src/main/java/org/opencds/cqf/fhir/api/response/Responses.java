package org.opencds.cqf.fhir.api.response;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.opencds.cqf.fhir.api.error.Error;
import org.opencds.cqf.fhir.api.error.Errors;
import org.opencds.cqf.fhir.api.header.Headers;
import org.opencds.cqf.fhir.api.status.HttpStatus;

/**
 * Companion class for Response, containing factories, builders, and interfaces
 */
public class Responses {

  private Responses() {
    // intentionally empty
  }

  @FunctionalInterface
  public interface NextResponse<R extends IBaseResource, T extends IBaseResource> {
    Response<T> execute(Response<R> current);
  }

  @FunctionalInterface
  public interface NextResource<R extends IBaseResource, T extends IBaseResource> {
    T execute(R current);
  }

  @FunctionalInterface
  public interface NextResponseFromResource<R extends IBaseResource, T extends IBaseResource> {
    Response<T> execute(R current);
  }

  public static <R extends IBaseResource> Response<R> ok() {
    return new Response<>(Optional.empty(), null, HttpStatus.OK, new Headers());
  }

  public static <R extends IBaseResource> Response<R> ok(R resource) {
    checkNotNull(resource);
    return new Response<>(Optional.of(resource), null, HttpStatus.OK, new Headers());
  }

  public static <R extends IBaseResource> Response<R> error(Throwable t) {
    return error(Errors.forException(t));
  }

  public static <R extends IBaseResource> Response<R> error(String m) {
    return error(Errors.forMessage(m));
  }

  public static <R extends IBaseResource> Response<R> error(Error e) {
    return error(e, HttpStatus.INTERNAL_SERVER_ERROR, new Headers());
  }

  public static <R extends IBaseResource> Response<R> error(Error e, HttpStatus httpStatus,
      Headers headers) {
    return new Response<>(null, e, httpStatus, headers);
  }

  static <T extends IBaseResource, R extends IBaseResource> Response<T> tryNext(Response<R> r,
      NextResponse<R, T> next) {
    if (r.hasError()) {
      return error(r.error(), r.status(), r.headers());
    }

    try {
      return next.execute(r);
    } catch (Exception e) {
      return Responses.error(e);
    }
  }
}
