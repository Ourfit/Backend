package io.ourfit.api.global.data.dto;

import java.util.List;
import org.springframework.data.domain.Slice;

/**
 * 페이징[Slice]된 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record SliceResponse<T>(String message, SliceData<T> data) {

  /**
   * 응답 데이터가 없는 경우, 표준 응답 객체를 생성한다.
   *
   * @param <T> 응답 데이터의 타입
   * @return 데이터 없이 {@code OK} 메시지만 포함한 응답 객체
   */
  public static <T> SliceResponse<T> empty() {
    return new SliceResponse<>("OK", SliceData.empty());
  }

  /**
   * 요청 성공 시, 응답 데이터를 포함해 표준 응답 객체를 생성한다.
   *
   * @param slice 응답 데이터
   * @param <T> 응답 데이터의 타입
   * @return {@code OK} 메시지와 응답 데이터를 포함한 응답 객체
   */
  public static <T> SliceResponse<T> from(Slice<T> slice) {
    return new SliceResponse<>("OK", SliceData.from(slice));
  }

  /**
   * 페이징[Slice]된 응답 객체
   *
   * @param hasNext 다음 페이지 존재 여부
   * @param content 응답 데이터
   * @param <T>
   */
  private record SliceData<T>(boolean hasNext, List<T> content) {

    public static <T> SliceData<T> empty() {
      return new SliceData<>(false, null);
    }

    public static <T> SliceData<T> from(Slice<T> slice) {
      return new SliceData<>(slice.hasNext(), slice.getContent());
    }
  }
}
