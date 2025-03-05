package io.ourfit.api.domain.challenge.data.dto.internal;

import io.ourfit.api.domain.challenge.data.dto.request.ChallengeCreateRequest;
import io.ourfit.api.global.utils.StreamUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * 챌린지 생성 DTO
 *
 * @param mateId 메이트 ID
 * @param goalWorkoutCount 목표 운동 횟수
 * @param goalWorkoutDayOfWeeks 목표 운동 요일
 * @param challengeDurationInMonths 챌린지 기간(월)
 * @param startAt 시작일
 * @param endAt 종료일
 */
public record ChallengeCreateDto(
    long mateId,
    short goalWorkoutCount,
    Set<DayOfWeek> goalWorkoutDayOfWeeks,
    short challengeDurationInMonths,
    LocalDate startAt,
    LocalDate endAt) {

  public static ChallengeCreateDto fromRequest(ChallengeCreateRequest request) {
    return new ChallengeCreateDto(
        request.mateId(),
        request.goalWorkoutCount(),
        StreamUtils.mapToSet(request.goalWorkoutDayOfWeeks(), DayOfWeek::valueOf),
        request.challengeDurationInMonths(),
        request.startAt(),
        request.endAt());
  }
}
