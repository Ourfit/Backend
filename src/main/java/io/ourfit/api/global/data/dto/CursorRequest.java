package io.ourfit.api.global.data.dto;

import io.ourfit.api.global.data.Cursorable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

/**
 * {@link Slice} 처리를 위한 커서 기반 요청 객체
 *
 * @param lastId 이전 요청에서 마지막으로 조회된 항목의 ID (없을 경우 {@code null})
 * @param size 한 번에 조회할 항목의 개수
 */
public record CursorRequest(Long lastId, int size) implements Cursorable {

  @Override
  public Long getLastId() {
    return this.lastId;
  }

  @Override
  public int getPageNumber() {
    return 0;
  }

  @Override
  public int getPageSize() {
    return this.size;
  }

  @Override
  public long getOffset() {
    return 0L;
  }

  @Override
  public Sort getSort() {
    return Sort.unsorted();
  }

  @Override
  public Pageable next() {
    throw new UnsupportedOperationException(
        "Cursor-based pagination does not support 'next' operation.");
  }

  @Override
  public Pageable previousOrFirst() {
    return this;
  }

  @Override
  public Pageable first() {
    return this;
  }

  @Override
  public Pageable withPage(int pageNumber) {
    throw new UnsupportedOperationException(
        "Cursor-based pagination does not support 'withPage' operation.");
  }

  @Override
  public boolean hasPrevious() {
    return false;
  }
}
