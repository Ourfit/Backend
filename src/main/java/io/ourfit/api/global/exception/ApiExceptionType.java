package io.ourfit.api.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ApiExceptionType {
  BAD_REQUEST(400, "Bad Request", 400000),
  INVALID_PARAMETER(400, "Bad Request: Required parameters are invalid or missing.", 400001),

  UNAUTHORIZED(401, "Unauthorized", 401000),
  INVALID_TOKEN(401, "Unauthorized: Invalid JWT (expired or not matched)", 401001),

  FORBIDDEN(403, "Forbidden", 403000),

  NOT_FOUND(404, "Not Found", 404000),
  NOT_FOUND_USER(404, "Not Found: User not found", 404001),

  NOT_ALLOWED_METHOD(405, "Method Not Allowed", 405000),

  CONFLICT(409, "Conflict", 409000),
  RESOURCE_ALREADY_EXISTS(409, "Conflict: The requested resource already exists", 409001),
  RESOURCE_IDENTICAL(409, "Conflict: The requested resource is identical", 409002),

  TOO_MANY_REQUESTS(429, "Too Many Requests", 429000),

  INTERNAL_SERVER_ERROR(500, "Internal Server Error", 500000),
  ;

  /**
   * 각 예외 상황에 대한 적절한 HTTP Status Code
   *
   * @see HttpStatus
   */
  private final int statusCode;

  /** 클라이언트에게 전달할 예외 상황에 대한 정보 또는 메세지 */
  private final String message;

  /** 디버그, 내부 관리를 위한 에러 코드 */
  private final int code;
}
