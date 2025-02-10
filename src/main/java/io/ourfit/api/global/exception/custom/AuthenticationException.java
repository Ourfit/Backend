package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

/**
 * 인증 실패 시 발생하는 예외
 * <li>토큰 만료, OAuth2 인증 실패 등
 */
public class AuthenticationException extends OurfitApiException {

  public AuthenticationException(Throwable cause) {
    super(ApiExceptionType.UNAUTHORIZED, cause);
  }

  public AuthenticationException() {
    this(null);
  }
}
