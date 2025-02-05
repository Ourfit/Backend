package io.ourfit.api.domain.mate.data.dto.internal;

import io.ourfit.api.domain.mate.data.dto.request.MateWorkoutTimeUpsertRequest;
import io.ourfit.api.global.utils.StreamUtils;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * 메이트와 운동 선호 정보 수정 DTO
 *
 * @param workoutDays 함께 운동하는 요일
 * @param startAt 함께 운동 시작 시간
 * @param endAt 함께 운동 종료 시간
 */
public record MateWorkoutTimeUpsertDto(
    Set<DayOfWeek> workoutDays, LocalTime startAt, LocalTime endAt) {

  public static MateWorkoutTimeUpsertDto fromRequest(MateWorkoutTimeUpsertRequest request) {
    return new MateWorkoutTimeUpsertDto(
        StreamUtils.mapToSet(request.workoutDays(), DayOfWeek::valueOf),
        request.startAt(),
        request.endAt());
  }
}
