package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

/** 요청 파라미터가 null이거나, 잘못된 형식일 경우 던지는 예외 */
public class InvalidParameterException extends OurfitApiException {

  public InvalidParameterException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }

  public InvalidParameterException() {
    this(ApiExceptionType.INVALID_PARAMETER);
  }
}
