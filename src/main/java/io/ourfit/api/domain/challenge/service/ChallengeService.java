package io.ourfit.api.domain.challenge.service;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeCreateDto;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.user.data.entity.User;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChallengeService {

  /**
   * 챌린지를 생성한다.
   *
   * @param userId 챌린지를 생성하는 사용자 ID
   * @param challengeCreateDto 생성할 챌린지 정보
   */
  void create(long userId, ChallengeCreateDto challengeCreateDto);

  /**
   * 챌린지의 목표 요일을 (재)설정한다.
   *
   * @param challengeId 목표 요일을 설정할 챌린지 ID
   * @param goalDayOfWeeks 목표 요일
   */
  void setGoalDayOfWeeks(long challengeId, Set<DayOfWeek> goalDayOfWeeks);

  /**
   * 챌린지를 삭제한다.
   *
   * @param challengeId 삭제할 챌린지 ID
   */
  void delete(long challengeId);

  /**
   * 챌린지 ID로 챌린지를 조회한다.
   *
   * @param challengeId 조회할 챌린지 ID
   * @return 조회된 챌린지
   */
  Optional<Challenge> findById(long challengeId);

  /**
   * 사용자 정보로 현재 진행 중인 챌린지(챌린지 기록 포함)를 조회한다.
   *
   * @param userId 조회할 사용자 ID
   * @return 조회된 챌린지
   */
  List<Challenge> findAllByUser(User user);
}
