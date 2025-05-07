package io.ourfit.api.domain.challenge.data.dto.response;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.utils.StreamUtils;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 챌린지 정보 응답 DTO
 *
 * @param me 내 챌린지 정보
 * @param myMate 내 메이트의 챌린지 정보
 */
public record ChallengeInfoResponse(ChallengeDetail me, ChallengeDetail myMate) {

  public static ChallengeInfoResponse from(User currentUser, List<Challenge> challenges) {
    var partitioned =
        challenges.stream()
            .collect(
                Collectors.partitioningBy(
                    challenge -> challenge.isOwner(currentUser),
                    Collectors.mapping(ChallengeDetail::of, Collectors.reducing((a, b) -> a))));
    return new ChallengeInfoResponse(
        partitioned.get(true).orElse(null), partitioned.get(false).orElse(null));
  }

  /**
   * 챌린지 정보 상세 DTO
   *
   * @param challengeId 챌린지 ID
   * @param dayElapsed 진행 일수
   * @param completionRate 목표 달성률
   * @param remainingDays 종료까지 남은 일수
   * @param goalWorkoutDayOfWeeks 목표 운동 요일
   * @param startAt 시작일
   * @param endAt 종료일
   */
  private record ChallengeDetail(
      long challengeId,
      long dayElapsed,
      long completionRate,
      long remainingDays,
      Set<String> goalWorkoutDayOfWeeks,
      String startAt,
      String endAt) {

    public static ChallengeDetail of(Challenge challenge) {
      return new ChallengeDetail(
          challenge.getId(),
          challenge.calculateDayElapsed(),
          challenge.calculateCompletionRate(),
          challenge.calculateRemainingDays(),
          StreamUtils.mapToSet(challenge.getGoalWorkoutDayOfWeeks(), DayOfWeek::name),
          challenge.getStartAt().toString(),
          challenge.getEndAt().toString());
    }
  }
}
