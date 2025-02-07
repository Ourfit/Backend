package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

public class IllegalEntityStateException extends OurfitApiException {

  public IllegalEntityStateException() {
    super(ApiExceptionType.ENTITY_ILLEGAL_STATE);
  }
}
