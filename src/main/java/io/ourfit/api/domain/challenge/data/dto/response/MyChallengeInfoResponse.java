package io.ourfit.api.domain.challenge.data.dto.response;

import io.ourfit.api.domain.challenge.data.entity.Challenge;

/**
 * 내 챌린지 정보 응답 DTO
 *
 * @param challengeId 챌린지 ID
 * @param dayElapsed 진행 일수
 * @param completionRate 목표 달성률
 * @param remainingDays 종료까지 남은 일수
 * @param startAt 시작일
 * @param endAt 종료일
 */
public record MyChallengeInfoResponse(
    long challengeId,
    long dayElapsed,
    long completionRate,
    long remainingDays,
    String startAt,
    String endAt) {

  public static MyChallengeInfoResponse from(Challenge challenge) {
    return new MyChallengeInfoResponse(
        challenge.getId(),
        challenge.calculateDayElapsed(),
        challenge.calculateCompletionRate(),
        challenge.calculateRemainingDays(),
        challenge.getStartAt().toString(),
        challenge.getEndAt().toString());
  }
}
