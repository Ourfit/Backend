package io.ourfit.api.global.data.dto;

import io.ourfit.api.global.data.ApiResponse;

/**
 * 단일 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record SingleResponse<T>(String message, T data) implements ApiResponse<T> {

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public T getData() {
    return this.data;
  }
}
