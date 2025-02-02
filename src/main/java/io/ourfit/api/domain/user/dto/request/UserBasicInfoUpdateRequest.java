package io.ourfit.api.domain.user.dto.request;

import io.ourfit.api.domain.user.dto.internal.UserBasicInfoUpdateDto;
import io.ourfit.api.domain.user.entity.enums.SkillLevelType;
import io.ourfit.api.domain.user.validation.Age;
import io.ourfit.api.domain.user.validation.Nickname;
import io.ourfit.api.global.validation.Enumerable;

/**
 * 사용자의 기본 정보 수정 요청 DTO
 *
 * @param nickname 변경할 닉네임
 * @param age 변경할 나이
 * @param region1 변경할 지역(시/도)
 * @param region2 변경할 지역(시/군/구)
 * @param region3 변경할 지역(동/읍/면)
 * @param skillLevel 변경할 운동 실력
 */
public record UserBasicInfoUpdateRequest(
    @Nickname(required = false) String nickname,
    @Age(required = false) Integer age,
    String region1,
    String region2,
    String region3,
    @Enumerable(targetClass = SkillLevelType.class, required = false) String skillLevel) {

  public UserBasicInfoUpdateDto toDto() {
    return new UserBasicInfoUpdateDto(
        this.nickname,
        this.age,
        this.region1,
        this.region2,
        this.region3,
        SkillLevelType.valueOf(this.skillLevel));
  }
}
