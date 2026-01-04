package io.ourfit.api.global.data.dto;

import static io.ourfit.api.global.exception.ApiExceptionType.*;

import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.OurfitApiException;
import java.util.List;
import org.springframework.http.HttpStatus;

/**
 * API 오류(예외) 응답 객체
 *
 * @param message HTTP Status Message(e.g., "Not Found", "Bad Request")
 * @param error 자세한 예외 정보
 * @see ApiExceptionType 예외 타입
 */
public record ErrorResponse(String message, ErrorData error) {

  public static ErrorResponse fromException(OurfitApiException ex, String resolvedMessage) {
    return new ErrorResponse(
        ex.getHttpStatus().getReasonPhrase(), ErrorData.of(resolvedMessage, ex.getCode()));
  }

  public static ErrorResponse badRequest(String message) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST.getReasonPhrase(), ErrorData.of(message, BAD_REQUEST.getCode()));
  }

  public static ErrorResponse badRequest(List<FieldError> errors) {
    return new ErrorResponse(
        HttpStatus.BAD_REQUEST.getReasonPhrase(),
        ErrorData.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), BAD_REQUEST.getCode(), errors));
  }

  public static ErrorResponse unauthorized(String message) {
    return new ErrorResponse(
        HttpStatus.UNAUTHORIZED.getReasonPhrase(), ErrorData.of(message, UNAUTHORIZED.getCode()));
  }

  public static ErrorResponse notFound(String message) {
    return new ErrorResponse(
        HttpStatus.NOT_FOUND.getReasonPhrase(), ErrorData.of(message, NOT_FOUND.getCode()));
  }

  public static ErrorResponse methodNotAllowed(String message) {
    return new ErrorResponse(
        HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
        ErrorData.of(message, NOT_ALLOWED_METHOD.getCode()));
  }

  public static ErrorResponse internalServerError(String message) {
    return new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        ErrorData.of(message, INTERNAL_SERVER_ERROR.getCode()));
  }
}
