package io.ourfit.api.domain.mate.data.dto.response;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistoryDto;

/**
 * 메이트 내역 응답 DTO
 *
 * @param id 내역 ID
 * @param mateId 메이트 ID
 * @param actionType 액션 타입
 * @param roleType 역할 타입 (요청 사용자가 ACTOR인지 TARGET인지)
 * @param isRead 읽음 여부 (이 API를 요청한 사용자가 읽었는지)
 * @param actorId 행동을 수행한 사용자 ID
 * @param actorNickname 행동을 수행한 사용자 닉네임
 * @param actorProfileImageUrl 행동을 수행한 사용자 프로필 이미지 URL
 * @param targetId 행동의 대상 사용자 ID
 * @param targetNickname 행동의 대상 사용자 닉네임
 * @param targetProfileImageUrl 행동의 대상 사용자 프로필 이미지 URL
 * @param createdAt 생성일시
 */
public record MateHistoryResponse(
    long id,
    long mateId,
    String actionType,
    String roleType,
    boolean isRead,
    long actorId,
    String actorNickname,
    String actorProfileImageUrl,
    long targetId,
    String targetNickname,
    String targetProfileImageUrl,
    String createdAt) {

  public static MateHistoryResponse from(MateHistoryDto dto) {
    return new MateHistoryResponse(
        dto.id(),
        dto.mateId(),
        dto.resolveActionType().name(),
        dto.roleType(),
        dto.isRead(),
        dto.actorId(),
        dto.actorNickname(),
        dto.actorProfileImageUrl(),
        dto.targetId(),
        dto.targetNickname(),
        dto.targetProfileImageUrl(),
        dto.createdAt().toString());
  }
}
