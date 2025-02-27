package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

/** Entity의 상태가 비정상적이거나, 요청을 처리할 수 없는 상태일 때 발생하는 예외 */
public class IllegalEntityStateException extends OurfitApiException {

  public IllegalEntityStateException() {
    super(ApiExceptionType.ENTITY_ILLEGAL_STATE);
  }
}
