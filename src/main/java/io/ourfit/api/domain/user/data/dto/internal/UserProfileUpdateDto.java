package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.user.data.dto.request.UserProfileUpdateRequest;

/**
 * 사용자 프로필 업데이트 DTO
 *
 * @param introduction 자기소개
 * @param openChatUrl 오픈채팅 URL
 */
public record UserProfileUpdateDto(String introduction, String openChatUrl) {

  public static UserProfileUpdateDto fromRequest(UserProfileUpdateRequest request) {
    return new UserProfileUpdateDto(request.introduction(), request.openChatUrl());
  }
}
