package io.ourfit.api.domain.user.service;

import io.ourfit.api.domain.user.data.dto.internal.*;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.Cursorable;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/** 사용자 정보 조회를 처리하는 서비스 인터페이스 */
public interface UserQueryService {

  /**
   * 메이트 관련 사용자 정보를 조회한다.
   *
   * @param requestedUser 요청한 사용자
   * @param searchDto 조회 조건
   * @param pageable 페이지 정보
   * @return 조회된 사용자 정보
   */
  Page<UserInfoDto> findMateCandidates(
      User requestedUser, MateCandidateSearchDto searchDto, Pageable pageable);

  /**
   * 메이트 관련 사용자 정보를 조회한다.
   *
   * @param requestedUser 요청한 사용자
   * @param searchDto 조회 조건
   * @param cursorable 커서 정보
   * @return 조회된 사용자 정보
   */
  Slice<UserInfoDto> findMateCandidates(
      User requestedUser, MateCandidateSearchDto searchDto, Cursorable cursorable);

  /**
   * 사용자 ID로 활성 사용자 정보를 조회한다.
   *
   * @param id 사용자 ID
   * @return 조회된 사용자 정보 (없을 경우 {@link Optional#empty()})
   * @apiNote 탈퇴한 사용자는 검색 대상에서 제외됩니다.
   */
  Optional<User> findById(long id);

  /**
   * 사용자 ID로 사용자 정보를 조회한다.
   *
   * @param id 사용자 ID
   * @param includeDeleted 탈퇴한 사용자도 조회할지 여부
   * @return 조회된 사용자 정보 (없을 경우 {@link Optional#empty()})
   */
  Optional<User> findById(long id, boolean includeDeleted);

  /**
   * 사용자 ID로 사용자 정보, 선호하는 운동 정보를 함께 조회한다.
   *
   * @param id 사용자 ID
   * @return 조회된 사용자 정보 (없을 경우 {@link Optional#empty()})
   */
  Optional<User> findByIdWithFavorites(long id);

  /**
   * OAuth ID로 사용자 정보를 조회한다.
   *
   * @param oAuthId OAuth ID (never {@code null})
   * @return 조회된 사용자 정보 (없을 경우 {@link Optional#empty()})
   */
  Optional<User> findByOAuthId(@NotNull String oAuthId);

  /**
   * 특정 OAuth ID를 가진 활성 사용자가 존재하는지 확인한다.
   *
   * @param oAuthId 확인할 OAuth ID (never {@code null})
   * @return 존재 여부
   * @apiNote 탈퇴한 사용자는 검색 대상에서 제외됩니다.
   */
  boolean existsByOAuthId(@NotNull String oAuthId);

  /**
   * 특정 닉네임을 가진 활성 사용자가 존재하는지 확인한다.
   *
   * @param nickname 확인할 닉네임 (never {@code null})
   * @return 존재 여부
   * @apiNote 탈퇴한 사용자는 검색 대상에서 제외됩니다.
   */
  boolean existsByNickname(@NotNull String nickname);
}
