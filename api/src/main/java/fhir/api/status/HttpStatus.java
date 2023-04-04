package fhir.api.status;

public enum HttpStatus {
  UNKNOWN(Integer.MAX_VALUE),
  OK(200),
  CREATED(201),
  NOT_FOUND(404),
  UNAUTHORIZED(401),
  INTERNAL_SERVER_ERROR(500);

  // TODO: A bunch of other codes

  private final int code;

  private HttpStatus(int code) {
    this.code = code;
  }

  public int code() {
    return this.code;
  }

  public static HttpStatus fromCode(int code) {
    for (var status : HttpStatus.values()) {
      if (status.code == code) {
        return status;
      }
    }

    return HttpStatus.UNKNOWN;
  }

  public static final int INFORMATION_RANGE_START = 100;
  public static final int OK_RANGE_START = 200;
  public static final int REDIRECT_RANGE_START = 300;
  public static final int CLIENT_ERROR_RANGE_START = 400;
  public static final int SERVER_ERROR_RANGE_START = 400;

}
