package io.ourfit.api.domain.mate.data.dto.response;

import io.ourfit.api.domain.mate.data.entity.Mate;

/**
 * 메이트 정보 응답 DTO
 *
 * @param mateUserId 내 메이트 사용자 ID
 * @param daySinceAccepted 메이트 요청을 수락해 메이트가 된 후 경과한 일 수
 */
public record MateInfoResponse(String status, long mateUserId, long daySinceAccepted) {

  public static MateInfoResponse from(Mate mate) {
    return new MateInfoResponse(
        mate.getStatusType().name(), mate.getMyMate().getId(), mate.getDaysSinceAccepted());
  }
}
