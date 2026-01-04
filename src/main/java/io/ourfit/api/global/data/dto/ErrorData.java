package io.ourfit.api.global.data.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * 예외 발생 시 클라이언트에게 전달할 메세지, 코드 정보 DTO
 *
 * @param message 메세지
 * @param code 코드
 * @param errors 오류 정보
 */
public record ErrorData(
    String message,
    Integer code,
    @JsonInclude(JsonInclude.Include.NON_EMPTY) List<FieldError> errors) {

  public static ErrorData of(String message, Integer code) {
    return new ErrorData(message, code, null);
  }

  public static ErrorData of(String message, Integer code, List<FieldError> errors) {
    return new ErrorData(message, code, errors);
  }
}
