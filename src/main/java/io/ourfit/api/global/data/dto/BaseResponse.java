package io.ourfit.api.global.data.dto;

import jakarta.annotation.Nullable;

/**
 * 기본 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record BaseResponse<T>(String message, T data) {

  /**
   * 응답 데이터가 없는 경우, 표준 응답 객체를 생성한다.
   *
   * @param <T> 응답 데이터의 타입
   * @return 데이터 없이 {@code OK} 메시지만 포함한 응답 객체
   */
  public static <T> BaseResponse<T> empty() {
    return new BaseResponse<>("OK", null);
  }

  /**
   * 요청 성공 시, 응답 데이터를 포함해 표준 응답 객체를 생성한다.
   *
   * @param data 응답 데이터
   * @param <T> 응답 데이터의 타입
   * @return {@code OK} 메시지와 응답 데이터를 포함한 응답 객체
   */
  public static <T> BaseResponse<T> from(@Nullable final T data) {
    return new BaseResponse<>("OK", data);
  }
}
