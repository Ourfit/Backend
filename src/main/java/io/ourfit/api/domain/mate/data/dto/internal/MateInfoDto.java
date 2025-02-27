package io.ourfit.api.domain.mate.data.dto.internal;

import io.ourfit.api.domain.mate.data.enums.MateStatusType;

/**
 * 메이트 정보 DTO
 *
 * @param mateId 메이트 ID
 * @param status 메이트 상태
 * @param daySinceAccepted 수락된 후 지난 일 수
 * @param myMate 나의 메이트 정보
 * @param workout 메이트와 함께 운동 정보
 */
public record MateInfoDto(
    long mateId,
    MateStatusType status,
    int daySinceAccepted,
    MyMateInfoDto myMate,
    MateWorkoutDto workout) {}
