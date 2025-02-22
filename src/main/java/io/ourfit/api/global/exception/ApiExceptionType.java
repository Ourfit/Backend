package io.ourfit.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiExceptionType {
  BAD_REQUEST(400, 400000, "io.ourfit.api.exception.BAD_REQUEST.message"),
  INVALID_PARAMETER(400, 400001, "io.ourfit.api.exception.INVALID_PARAMETER.message"),
  INVALID_DATE_FORMAT(400, 400002, "io.ourfit.api.exception.INVALID_DATE_FORMAT.message"),
  INVALID_DATE_RANGE(400, 400003, "io.ourfit.api.exception.INVALID_DATE_RANGE.message"),
  INVALID_REGION(400, 400004, "io.ourfit.api.exception.INVALID_REGION.message"),

  UNAUTHORIZED(401, 401000, "io.ourfit.api.exception.UNAUTHORIZED.message"),
  INVALID_TOKEN(401, 401001, "io.ourfit.api.exception.INVALID_TOKEN.message"),

  FORBIDDEN(403, 403000, "io.ourfit.api.exception.FORBIDDEN.message"),

  NOT_FOUND(404, 404000, "io.ourfit.api.exception.NOT_FOUND.message"),
  NOT_FOUND_API_ENDPOINT(404, 404001, "io.ourfit.api.exception.NOT_FOUND_API_ENDPOINT.message"),
  NOT_FOUND_USER(404, 404002, "io.ourfit.api.exception.NOT_FOUND_USER.message"),
  NOT_FOUND_MATE(404, 404003, "io.ourfit.api.exception.NOT_FOUND_MATE.message"),
  NOT_FOUND_MATE_WORKOUT(404, 404004, "io.ourfit.api.exception.NOT_FOUND_MATE_WORKOUT.message"),
  NOT_FOUND_CHALLENGE(404, 404005, "io.ourfit.api.exception.NOT_FOUND_CHALLENGE.message"),

  NOT_ALLOWED_METHOD(405, 405000, "io.ourfit.api.exception.NOT_ALLOWED_METHOD.message"),

  CONFLICT(409, 409000, "io.ourfit.api.exception.CONFLICT.message"),
  RESOURCE_ALREADY_EXISTS(409, 409001, "io.ourfit.api.exception.RESOURCE_ALREADY_EXISTS.message"),
  RESOURCE_IDENTICAL(409, 409002, "io.ourfit.api.exception.RESOURCE_IDENTICAL.message"),
  ENTITY_ILLEGAL_STATE(409, 409003, "io.ourfit.api.exception.ENTITY_ILLEGAL_STATE.message"),

  TOO_MANY_REQUESTS(429, 429000, "io.ourfit.api.exception.TOO_MANY_REQUESTS.message"),

  INTERNAL_SERVER_ERROR(500, 500000, "io.ourfit.api.exception.INTERNAL_SERVER_ERROR.message"),
  S3_ERROR(500, 500001, "io.ourfit.api.exception.S3_ERROR.message"),
  ;

  /**
   * 각 예외 상황에 대한 적절한 HTTP Status Code
   *
   * @see HttpStatus
   */
  private final int statusCode;

  /** 디버그, 내부 관리를 위한 에러 코드 */
  private final int code;

  /** 클라이언트에게 전달할 예외 상황에 대한 정보 또는 메세지의 키 */
  private final String messageKey;
}
