package io.ourfit.api.infra.persistence.user;

import io.ourfit.api.domain.user.data.dto.internal.MateCandidateSearchDto;
import io.ourfit.api.domain.user.data.dto.internal.UserInfoDto;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.Cursorable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface UserQRepository {

  /**
   * 사용자 검색 조건에 따라 사용자 목록을 조회한다.
   *
   * @param requestedUser 요청 사용자
   * @param searchDto 사용자 검색 조건 DTO
   * @param pageable offset-based 페이징 정보
   * @return 조회된 사용자 목록
   */
  Page<UserInfoDto> findMateCandidates(
      User requestedUser, MateCandidateSearchDto searchDto, Pageable pageable);

  /**
   * 사용자 검색 조건에 따라 사용자 목록을 조회한다.
   *
   * @param requestedUser 요청 사용자
   * @param searchDto 사용자 검색 조건 DTO
   * @param cursorable cursor-based 페이징 정보
   * @return 조회된 사용자 목록
   */
  Slice<UserInfoDto> findMateCandidates(
      User requestedUser, MateCandidateSearchDto searchDto, Cursorable cursorable);
}
