package io.ourfit.api.domain.mate.data.dto.response;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;

/**
 * 메이트 내역 응답 DTO
 *
 * @param id 내역 ID
 * @param actionType 액션 타입
 * @param isRead 읽음 여부 (이 API를 요청한 사용자가 읽었는지)
 * @param actorId 행동을 수행한 사용자 ID
 * @param actorNickname 행동을 수행한 사용자 닉네임
 * @param targetId 행동의 대상 사용자 ID
 * @param targetNickname 행동의 대상 사용자 닉네임
 * @param createdAt 생성일시
 */
public record MateHistoryResponse(
    long id,
    String actionType,
    boolean isRead,
    long actorId,
    String actorNickname,
    long targetId,
    String targetNickname,
    String createdAt) {

  public static MateHistoryResponse from(MateHistoryDto dto) {
    return new MateHistoryResponse(
        dto.id(),
        dto.actionType().name(),
        dto.isRead(),
        dto.actorId(),
        dto.actorNickname(),
        dto.targetId(),
        dto.targetNickname(),
        dto.createdAt().toString());
  }
}
