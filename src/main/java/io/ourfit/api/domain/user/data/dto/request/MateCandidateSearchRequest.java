package io.ourfit.api.domain.user.data.dto.request;

import io.ourfit.api.domain.workout.data.enums.TimePrefrenceType;
import io.ourfit.api.global.web.validation.Enumerable;
import java.util.Set;

/**
 * 사용자 검색 요청 DTO
 *
 * @param gender 조회할 성별
 * @param peferredTimes 조회할 선호 시간대
 * @param workoutTypes 조회할 운동 종류
 */
public record MateCandidateSearchRequest(
    String gender,
    @Enumerable(type = TimePrefrenceType.class, required = false) Set<String> peferredTimes,
    Set<String> workoutTypes) {}
