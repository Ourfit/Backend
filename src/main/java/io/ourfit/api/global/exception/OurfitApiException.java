package io.ourfit.api.global.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/** Ourfit API 내에서 발생하는 모든 예외의 최상위 클래스 */
@Getter
public abstract class OurfitApiException extends RuntimeException {

  protected static final Logger log = LoggerFactory.getLogger(OurfitApiException.class);

  /** 예외 타입 */
  private final ApiExceptionType apiExceptionType;

  /** HTTP 응답 코드 */
  private final HttpStatus httpStatus;

  /** 클라이언트에게 전달할 예외 상황에 대한 정보 또는 메세지의 키 */
  private final String messageKey;

  /** 예외 관리 코드 */
  private final Integer code;

  /** 예외 원인 */
  @Nullable private final Throwable cause;

  protected OurfitApiException(ApiExceptionType exceptionType, @Nullable Throwable cause) {
    super(exceptionType.name(), cause);
    this.apiExceptionType = exceptionType;
    this.httpStatus = HttpStatus.resolve(exceptionType.getStatusCode());
    this.messageKey = exceptionType.getMessageKey();
    this.code = exceptionType.getCode();
    this.cause = cause;
  }

  protected OurfitApiException(
      ApiExceptionType exceptionType, @Nullable String message, @Nullable Throwable cause) {
    super(message, cause);
    this.apiExceptionType = exceptionType;
    this.httpStatus = HttpStatus.resolve(exceptionType.getStatusCode());
    this.messageKey = exceptionType.getMessageKey();
    this.code = exceptionType.getCode();
    this.cause = cause;
  }

  protected OurfitApiException(ApiExceptionType exceptionType) {
    this(exceptionType, null);
  }

  public boolean is5xxServerError() {
    return this.httpStatus.is5xxServerError();
  }
}
