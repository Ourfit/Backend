package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.user.dto.internal.UserInfoDto;
import io.ourfit.api.domain.user.dto.internal.UserSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQRepository {

  /**
   * 사용자 검색 조건에 따라 사용자 목록을 조회한다.
   *
   * @param searchDto 사용자 검색 조건 DTO
   * @param pageable 페이징 정보
   * @return 조회된 사용자 목록
   */
  Page<UserInfoDto> findAllByConditions(UserSearchDto searchDto, Pageable pageable);
}
