package io.ourfit.api.domain.user.dto.internal;

/**
 * 사용자 프로필 업데이트 DTO
 *
 * @param introduction 자기소개
 * @param openChatUrl 오픈채팅 URL
 */
public record UserProfileUpdateDto(String introduction, String openChatUrl) {}
