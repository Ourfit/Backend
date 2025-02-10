package io.ourfit.api.global.data.dto;

import static io.ourfit.api.global.exception.ApiExceptionType.*;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;

/**
 * API 오류(예외) 응답 객체
 *
 * @param message HTTP Status Message(e.g., "Not Found", "Bad Request")
 * @param data 자세한 예외 정보
 * @see ApiExceptionType 예외 타입
 */
public record ErrorResponse(String message, ErrorData data) {

  public static ErrorResponse fromException(OurfitApiException ex) {
    return new ErrorResponse(
        ex.getHttpStatus().getReasonPhrase(), ErrorData.of(ex.getMessage(), ex.getCode()));
  }

  public static ErrorResponse badRequest(String message) {
    return new ErrorResponse(
        BAD_REQUEST.getMessage(), ErrorData.of(message, BAD_REQUEST.getCode()));
  }

  public static ErrorResponse notFound(String message) {
    return new ErrorResponse(NOT_FOUND.getMessage(), ErrorData.of(message, NOT_FOUND.getCode()));
  }

  public static ErrorResponse methodNotAllowed(String message) {
    return new ErrorResponse(
        NOT_ALLOWED_METHOD.getMessage(), ErrorData.of(message, NOT_ALLOWED_METHOD.getCode()));
  }

  public static ErrorResponse internalServerError() {
    return new ErrorResponse(
        INTERNAL_SERVER_ERROR.getMessage(),
        ErrorData.of("Internal Server Error", INTERNAL_SERVER_ERROR.getCode()));
  }
}
