package io.ourfit.api.global.data;

import io.ourfit.api.global.data.dto.ListResponse;
import io.ourfit.api.global.data.dto.PageResponse;
import io.ourfit.api.global.data.dto.SingleResponse;
import io.ourfit.api.global.data.dto.SliceResponse;
import java.util.List;
import org.springframework.data.domain.Slice;

/**
 * 표준 요청 성공 응답 인터페이스
 *
 * @param <T> 응답 데이터의 타입
 * @see SingleResponse 단일 응답 객체
 * @see SliceResponse 페이징[Slice] 응답 객체
 * @see PageResponse 페이지[Page] 응답 객체
 */
public interface ApiResponse<T> {

  /** 표준 응답 메시지 */
  String MESSAGE_OK = "OK";

  /**
   * 응답 메시지를 반환한다.
   *
   * @return 응답 메시지(주로 {@code "OK"})
   */
  String getMessage();

  /**
   * 응답 데이터를 반환한다.
   *
   * @return 응답 데이터
   */
  T getData();

  static <T> SingleResponse<T> ok() {
    return new SingleResponse<>(MESSAGE_OK, null);
  }

  static <T> SingleResponse<T> of(T data) {
    return new SingleResponse<>(MESSAGE_OK, data);
  }

  static <T> ListResponse<T> of(List<T> data) {
    return new ListResponse<>(MESSAGE_OK, data);
  }

  static <T> SliceResponse<T> of(Slice<T> data) {
    return new SliceResponse<>(MESSAGE_OK, SliceResponse.SliceData.from(data));
  }

  static <T> PageResponse<T> of(org.springframework.data.domain.Page<T> data) {
    return new PageResponse<>(MESSAGE_OK, PageResponse.PageData.from(data));
  }
}
