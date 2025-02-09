package io.ourfit.api.domain.mate.data.dto.internal;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

/**
 * 메이트 운동 정보 DTO
 *
 * @param placeName 운동 장소 이름
 * @param address 운동 장소 주소
 * @param workoutDayOfWeek 운동 요일
 * @param workoutStartAt 운동 시작 시간
 * @param workoutEndAt 운동 종료 시간
 */
public record MateWorkoutDto(
    String placeName,
    String address,
    Set<DayOfWeek> workoutDayOfWeek,
    LocalTime workoutStartAt,
    LocalTime workoutEndAt) {}
