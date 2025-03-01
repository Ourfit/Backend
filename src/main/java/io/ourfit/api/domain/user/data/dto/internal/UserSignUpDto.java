package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.mate.data.enums.TimePrefrenceType;
import io.ourfit.api.domain.user.data.dto.request.UserSignUpRequest;
import io.ourfit.api.domain.user.data.entity.enums.GenderType;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.data.entity.enums.SkillLevelType;
import java.util.Set;

/**
 * 사용자 회원가입 처리 DTO
 *
 * @param oAuthId OAuth2 ID
 * @param code Ourfit 인가 코드
 * @param providerType OAuth2 제공자 유형
 * @param nickname 사용할 닉네임
 * @param region1 사용자의 지역(시/도)
 * @param region2 사용자의 지역(시/군/구)
 * @param region3 사용자의 지역(읍/면/동)
 * @param gender 사용자의 성별
 * @param age 사용자의 나이
 * @param skillLevel 선택한 운동 실력 수준
 * @param preferredWorkoutTime 선호하는 운동 시간대
 * @param favoriteWorkouts 선호하는 운동 종목
 */
public record UserSignUpDto(
    String oAuthId,
    String code,
    OAuth2ProviderType providerType,
    String nickname,
    String region1,
    String region2,
    String region3,
    GenderType gender,
    Integer age,
    SkillLevelType skillLevel,
    TimePrefrenceType preferredWorkoutTime,
    Set<String> favoriteWorkouts) {

  public static UserSignUpDto fromRequest(UserSignUpRequest request) {
    return new UserSignUpDto(
        request.oAuthId(),
        request.code(),
        OAuth2ProviderType.valueOf(request.provider()),
        request.nickname(),
        request.region1(),
        request.region2(),
        request.region3(),
        GenderType.valueOf(request.gender()),
        request.age(),
        SkillLevelType.valueOf(request.skillLevel()),
        TimePrefrenceType.valueOf(request.preferredWorkoutTime()),
        request.favoriteWorkouts());
  }
}
