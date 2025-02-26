package io.ourfit.api.global.data.dto;

import io.ourfit.api.global.data.ApiResponse;
import java.util.List;

/**
 * 리스트 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record ListResponse<T>(String message, List<T> data) implements ApiResponse<List<T>> {

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public List<T> getData() {
    return this.data;
  }
}
