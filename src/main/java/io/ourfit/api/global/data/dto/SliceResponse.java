package io.ourfit.api.global.data.dto;

import io.ourfit.api.global.data.ApiResponse;
import java.util.List;
import java.util.function.ToLongFunction;
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
   * @param <T> 응답 데이터의 타입
   */
  public record SliceData<T>(Long lastId, boolean hasNext, List<T> content) {

    public static <T> SliceData<T> empty() {
      return new SliceData<>(null, false, List.of());
    }

    public static <T> SliceData<T> from(Slice<T> slice, ToLongFunction<T> idExtractor) {
      List<T> content = slice.getContent();
      return new SliceData<>(extractLastId(content, idExtractor), slice.hasNext(), content);
    }

    private static <T> Long extractLastId(List<T> content, ToLongFunction<T> idExtractor) {
      if (content.isEmpty()) {
        return null;
      }
      return idExtractor.applyAsLong(content.get(content.size() - 1));
    }
  }
}
