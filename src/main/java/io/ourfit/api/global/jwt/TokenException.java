package io.ourfit.api.global.jwt;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

public class TokenException extends OurfitApiException {

  public TokenException() {
    super(ApiExceptionType.INVALID_TOKEN);
  }
}
