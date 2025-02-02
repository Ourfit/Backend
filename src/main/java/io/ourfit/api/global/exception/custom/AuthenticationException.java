package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

public class AuthenticationException extends OurfitApiException {

  public AuthenticationException(Throwable cause) {
    super(ApiExceptionType.UNAUTHORIZED, cause);
  }

  public AuthenticationException() {
    this(null);
  }
}
