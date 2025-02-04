package io.ourfit.api.domain.user.data.dto.request;

import io.ourfit.api.domain.user.data.dto.internal.UserProfileUpdateDto;
import io.ourfit.api.domain.user.validation.OpenChatUrl;

/**
 * 사용자 프로필 업데이트 요청 DTO
 *
 * @param introduction 자기소개
 * @param openChatUrl 오픈채팅 URL
 */
public record UserProfileUpdateRequest(
    String introduction, @OpenChatUrl(required = false) String openChatUrl) {

  public UserProfileUpdateDto toDto() {
    return new UserProfileUpdateDto(this.introduction, this.openChatUrl);
  }
}
