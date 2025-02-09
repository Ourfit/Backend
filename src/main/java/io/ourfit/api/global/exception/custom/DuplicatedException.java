package io.ourfit.api.global.exception.custom;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;
import lombok.Getter;

/**
 * 중복된 리소스가 존재할 때 발생하는 예외 <br>
 * <li>이미 존재하는 사용자 정보로 회원가입을 시도할 때 등
 */
@Getter
public class DuplicatedException extends OurfitApiException {

  public DuplicatedException(ApiExceptionType apiExceptionType) {
    super(apiExceptionType);
  }

  public DuplicatedException() {
    this(ApiExceptionType.RESOURCE_ALREADY_EXISTS);
  }
}
