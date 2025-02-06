package io.ourfit.api.domain.challenge.data.dto.internal;

import io.ourfit.api.domain.challenge.data.dto.request.NewChallengeRequest;
import io.ourfit.api.global.utils.StreamUtils;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Set;

/**
 * 챌린지 생성 DTO
 *
 * @param mateId 메이트 ID
 * @param goalWorkoutCount 목표 운동 횟수
 * @param goalWorkoutDayOfWeek 목표 운동 요일
 * @param challengeDurationInMoths 챌린지 기간(월)
 * @param startAt 시작일
 * @param endAt 종료일
 */
public record NewChallengeDto(
    long mateId,
    short goalWorkoutCount,
    Set<DayOfWeek> goalWorkoutDayOfWeek,
    short challengeDurationInMoths,
    LocalDate startAt,
    LocalDate endAt) {

  public static NewChallengeDto fromRequest(NewChallengeRequest request) {
    return new NewChallengeDto(
        request.mateId(),
        request.goalWorkoutCount(),
        StreamUtils.mapToSet(request.goalWorkoutDayOfWeeks(), DayOfWeek::valueOf),
        request.challengeDurationInMoths(),
        LocalDate.parse(request.startAt()),
        LocalDate.parse(request.endAt()));
  }
}
