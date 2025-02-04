package io.ourfit.api.infra.exception;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

/** 파일 업로드/삭제 등 파일 작업에 실패했을 때 발생하는 예외 */
public class FileOperationException extends OurfitApiException {

  public FileOperationException(ApiExceptionType apiExceptionType, Throwable cause) {
    super(apiExceptionType, cause);
  }

  public FileOperationException(ApiExceptionType apiExceptionType) {
    this(apiExceptionType, null);
  }
}
