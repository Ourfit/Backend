package io.ourfit.api.global.data.dto;

import io.ourfit.api.global.data.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Slice;

/**
 * 페이징[Slice]된 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record SliceResponse<T>(String message, SliceData<T> data)
    implements ApiResponse<SliceResponse.SliceData<T>> {

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public SliceData<T> getData() {
    return this.data;
  }

  /**
   * 페이징[Slice]된 응답 객체
   *
   * @param hasNext 다음 페이지 존재 여부
   * @param content 응답 데이터
   * @param <T>
   */
  public record SliceData<T>(boolean hasNext, List<T> content) {

    public static <T> SliceData<T> empty() {
      return new SliceData<>(false, null);
    }

    public static <T> SliceData<T> from(Slice<T> slice) {
      return new SliceData<>(slice.hasNext(), slice.getContent());
    }
  }
}
