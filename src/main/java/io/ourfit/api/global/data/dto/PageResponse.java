package io.ourfit.api.global.data.dto;

import io.ourfit.api.global.data.ApiResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 페이징된 응답 객체
 *
 * @param message 응답 메시지
 * @param data 응답 데이터
 * @param <T> 응답 데이터의 타입
 */
public record PageResponse<T>(String message, PageData<T> data)
    implements ApiResponse<PageResponse.PageData<T>> {

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public PageData<T> getData() {
    return this.data;
  }

  /**
   * 페이징된 응답 객체
   *
   * @param totalPages 전체 페이지 수
   * @param totalElements 전체 요소 수
   * @param size 페이지 크기
   * @param content 응답 데이터
   * @param pageable 페이지 정보
   * @param hasNext 다음 페이지 존재 여부
   * @param hasPrevious 이전 페이지 존재 여부
   * @param isFirst 첫 페이지인지 여부
   * @param isLast 마지막 페이지인지 여부
   * @param isEmpty 비어있는 페이지인지 여부
   * @param <T> 응답 데이터의 타입
   */
  public record PageData<T>(
      int totalPages,
      long totalElements,
      int size,
      List<T> content,
      Pageable pageable,
      int number,
      int numberOfElements,
      boolean hasNext,
      boolean hasPrevious,
      boolean isFirst,
      boolean isLast,
      boolean isEmpty) {

    public static <T> PageData<T> empty() {
      return new PageData<>(0, 0, 0, null, null, 0, 0, false, false, false, false, true);
    }

    public static <T> PageData<T> from(Page<T> page) {
      return new PageData<>(
          page.getTotalPages(),
          page.getTotalElements(),
          page.getSize(),
          page.getContent(),
          page.getPageable(),
          page.getNumber(),
          page.getNumberOfElements(),
          page.hasNext(),
          page.hasPrevious(),
          page.isFirst(),
          page.isLast(),
          page.isEmpty());
    }
  }
}
