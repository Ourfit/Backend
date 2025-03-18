package io.ourfit.api.global.data;

import org.springframework.data.domain.Pageable;

/** 커서 기반 페이징을 지원하는 인터페이스 */
public interface Cursorable extends Pageable {

  /**
   * 이전 요청에서 마지막으로 조회된 커서를 반환한다.
   *
   * @return 마지막 조회된 항목의 ID (없을 경우 {@code null})
   */
  Long getCursor();
}
