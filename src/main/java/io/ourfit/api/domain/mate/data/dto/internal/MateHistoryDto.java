package io.ourfit.api.domain.mate.data.dto.internal;

import io.ourfit.api.domain.mate.data.enums.MateActionType;
import io.ourfit.api.domain.mate.data.enums.MateRoleType;

import java.time.LocalDateTime;

/**
 * 메이트 내역 DTO
 *
 * @param id 내역 ID
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
public record MateHistoryDto(
    long id,
    long mateId,
    MateActionType actionType,
    MateRoleType roleType,
    boolean isRead,
    long actorId,
    String actorNickname,
    String actorProfileImageUrl,
    long targetId,
    String targetNickname,
    String targetProfileImageUrl,
    LocalDateTime createdAt) {}
