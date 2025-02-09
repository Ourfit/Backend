package io.ourfit.api.domain.mate.data.dto.response;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutDto;
import io.ourfit.api.global.utils.StreamUtils;
import java.time.DayOfWeek;
import java.util.Objects;
import java.util.Set;

/**
 * 메이트 운동 정보 응답 DTO
 *
 * @param placeName 운동 장소 이름
 * @param address 운동 장소 주소
 * @param workoutDayOfWeek 운동 요일
 * @param workoutStartAt 운동 시작 시간
 * @param workoutEndAt 운동 종료 시간
 */
public record MateWorkoutResponse(
    String placeName,
    String address,
    Set<String> workoutDayOfWeek,
    String workoutStartAt,
    String workoutEndAt) {

  public static MateWorkoutResponse from(MateWorkoutDto dto) {
    return new MateWorkoutResponse(
        dto.placeName(),
        dto.address(),
        StreamUtils.mapToSet(dto.workoutDayOfWeek(), DayOfWeek::name),
        Objects.toString(dto.workoutStartAt(), null),
        Objects.toString(dto.workoutEndAt(), null));
  }
}
