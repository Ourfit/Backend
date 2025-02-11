package io.ourfit.api.domain.terms.service;

import io.ourfit.api.domain.terms.data.dto.internal.TermsUpsertDto;
import io.ourfit.api.domain.terms.data.entity.Terms;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import java.util.List;
import java.util.Optional;

public interface TermsService {

  /**
   * 새로 또는 수정된 약관을 등록한다.
   *
   * @param upsertDto 등록 또는 수정할 약관 정보에 대한 DTO
   * @apiNote {@code version}은 자동 부여됨
   */
  void upsert(TermsUpsertDto upsertDto);

  /**
   * 지정한 타입의 최신 버전 약관을 조회한다.
   *
   * @param termsType 약관 타입
   * @return 약관 정보
   */
  Optional<Terms> findByType(TermsType termsType);

  /**
   * 최신 버전의 모든 약관을 조회한다.
   *
   * @return 약관 목록
   */
  List<Terms> findAll();
}
