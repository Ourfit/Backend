package io.ourfit.api.domain.challenge.data.dto.response;

import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.user.data.entity.User;
import java.util.List;

/**
 * 챌린지 정보 응답 DTO
 *
 * @param myChallenge 내 챌린지 정보
 * @param myMateChallenge 내 메이트의 챌린지 정보
 */
public record ChallengeInfoResponse(ChallengeDetail myChallenge, ChallengeDetail myMateChallenge) {

  public static ChallengeInfoResponse from(User user, List<Challenge> challenges) {
    ChallengeDetail myChallenge =
        challenges.stream()
            .filter(challenge -> challenge.isOwner(user))
            .findFirst()
            .map(ChallengeDetail::of)
            .orElse(null);
    ChallengeDetail myMateChallenge =
        challenges.stream()
            .filter(challenge -> !challenge.isOwner(user))
            .findFirst()
            .map(ChallengeDetail::of)
            .orElse(null);
    return new ChallengeInfoResponse(myChallenge, myMateChallenge);
  }

  /**
   * 챌린지 정보 상세 DTO
   *
   * @param challengeId 챌린지 ID
   * @param dayElapsed 진행 일수
   * @param completionRate 목표 달성률
   * @param remainingDays 종료까지 남은 일수
   * @param startAt 시작일
   * @param endAt 종료일
   */
  private record ChallengeDetail(
      long challengeId,
      long dayElapsed,
      long completionRate,
      long remainingDays,
      String startAt,
      String endAt) {

    public static ChallengeDetail of(Challenge challenge) {
      return new ChallengeDetail(
          challenge.getId(),
          challenge.calculateDayElapsed(),
          challenge.calculateCompletionRate(),
          challenge.calculateRemainingDays(),
          challenge.getStartAt().toString(),
          challenge.getEndAt().toString());
    }
  }
}
