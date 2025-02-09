package io.ourfit.api.domain.mate.data.dto.response;

import io.ourfit.api.domain.mate.data.dto.internal.MateInfoDto;
import lombok.Builder;

/**
 * 메이트 정보 응답 DTO
 *
 * @param mateId 메이트 ID
 * @param status 메이트 상태
 * @param daySinceAccepted 메이트 요청을 수락해 메이트가 된 후 경과한 일 수
 */
@Builder
public record MateInfoResponse(
    long mateId,
    String status,
    int daySinceAccepted,
    MyMateInfoResponse myMate,
    MateWorkoutResponse workout) {

  public static MateInfoResponse from(MateInfoDto dto) {
    return MateInfoResponse.builder()
        .mateId(dto.mateId())
        .status(dto.status().name())
        .daySinceAccepted(dto.daySinceAccepted())
        .myMate(MyMateInfoResponse.from(dto.myMate()))
        .workout(MateWorkoutResponse.from(dto.workout()))
        .build();
  }
}
