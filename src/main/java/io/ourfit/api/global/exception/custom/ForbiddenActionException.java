package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

/** 권한 없는 경우 발생하는 예외 */
public class ForbiddenActionException extends OurfitApiException {

  public ForbiddenActionException(Throwable cause) {
    super(ApiExceptionType.FORBIDDEN, cause);
  }

  public ForbiddenActionException() {
    this(null);
  }
}
