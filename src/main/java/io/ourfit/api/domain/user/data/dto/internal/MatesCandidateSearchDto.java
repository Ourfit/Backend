package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.user.data.entity.enums.GenderType;
import io.ourfit.api.domain.workout.data.enums.TimePrefrenceType;
import java.util.List;
import java.util.Set;

/**
 * 사용자 검색 DTO
 *
 * @param region3 요청 사용자의 동네(동/읍/면)
 * @param gender 검색할 성별
 * @param preferredTimes 검색할 선호 시간대
 * @param workoutTypes 검색할 운동 종류
 */
public record MatesCandidateSearchDto(
    String region3,
    GenderType gender,
    List<TimePrefrenceType> preferredTimes,
    Set<String> workoutTypes) {}
