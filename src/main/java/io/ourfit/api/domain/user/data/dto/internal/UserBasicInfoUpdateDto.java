package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.user.data.dto.request.UserBasicInfoUpdateRequest;
import io.ourfit.api.domain.user.data.entity.enums.SkillLevelType;

/**
 * 사용자의 기본 정보 수정 DTO
 *
 * @param nickname 변경할 닉네임
 * @param age 변경할 나이
 * @param region1 변경할 지역(시/도)
 * @param region2 변경할 지역(시/군/구)
 * @param region3 변경할 지역(동/읍/면)
 * @param skillLevel 변경할 운동 실력
 */
public record UserBasicInfoUpdateDto(
    String nickname,
    Integer age,
    String region1,
    String region2,
    String region3,
    SkillLevelType skillLevel) {

  public static UserBasicInfoUpdateDto fromRequest(UserBasicInfoUpdateRequest request) {
    return new UserBasicInfoUpdateDto(
        request.nickname(),
        request.age(),
        request.region1(),
        request.region2(),
        request.region3(),
        SkillLevelType.findByName(request.skillLevel()).orElse(null));
  }
}
