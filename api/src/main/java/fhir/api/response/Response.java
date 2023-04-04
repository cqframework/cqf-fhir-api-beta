package fhir.api.response;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Optional;

import org.hl7.fhir.instance.model.api.IBaseResource;
import fhir.api.core.Either;
import fhir.api.error.Error;
import fhir.api.header.Headers;
import fhir.api.response.Responses.NextResource;
import fhir.api.response.Responses.NextResponse;
import fhir.api.response.Responses.NextResponseFromResource;
import fhir.api.status.HttpStatus;

public class Response<R extends IBaseResource> extends Either<Optional<R>, Error> {

  private final HttpStatus status;
  private final Headers headers;

  Response(Optional<R> resource, Error e, HttpStatus status, Headers headers) {
    super(resource, e);
    this.status = checkNotNull(status);
    this.headers = checkNotNull(headers);
  }

  public Headers headers() {
    return this.headers;
  }

  public HttpStatus status() {
    return this.status;
  }

  public boolean isOk() {
    return this.isLeft();
  }

  public boolean isEmpty() {
    return this.isLeft() && !this.left().isPresent();
  }

  public boolean hasResource() {
    return this.isLeft() && this.left().isPresent();
  }

  public boolean hasError() {
    return this.isRight();
  }

  public R resource() {
    return this.left().get();
  }

  public Error error() {
    return this.right();
  }

  public <T extends IBaseResource> Response<T> then(NextResponse<R, T> next) {
    return Responses.tryNext(this, next);
  }

  public <T extends IBaseResource> Response<T> then(NextResponseFromResource<R, T> next) {
    return Responses.tryNext(this, r -> next.execute(r.resource()));
  }

  public <T extends IBaseResource> Response<T> then(NextResource<R, T> next) {
    return Responses.tryNext(this, r -> Responses.ok(next.execute(r.resource())));
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + status.hashCode();
    result = prime * result + headers.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    Response<?> other = (Response<?>) obj;
    if (status != other.status)
      return false;
    return headers.equals(other.headers);
  }
}
