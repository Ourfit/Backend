package io.ourfit.api.domain.user.dto.request;

import io.ourfit.api.domain.user.dto.internal.UserSignUpDto;
import io.ourfit.api.domain.user.entity.enums.GenderType;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.entity.enums.SkillLevelType;
import io.ourfit.api.domain.user.validation.Age;
import io.ourfit.api.domain.user.validation.Nickname;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import io.ourfit.api.global.validation.Enumerable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.Set;

/**
 * 사용자 회원가입 요청 DTO
 *
 * @param code OAuth2 인가 코드
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
public record UserSignUpRequest(
    @NotEmpty String code,
    @Enumerable(targetClass = OAuth2ProviderType.class) String provider,
    @Nickname String nickname,
    @NotEmpty String region1,
    @NotEmpty String region2,
    @NotEmpty String region3,
    @Enumerable(targetClass = GenderType.class) String gender,
    @Age Integer age,
    @Enumerable(targetClass = SkillLevelType.class) String skillLevel,
    @Enumerable(targetClass = TimePrefrenceType.class) String preferredWorkoutTime,
    @Size(min = 1, max = 3) Set<String> favoriteWorkouts) {

  public UserSignUpDto toDto() {
    return new UserSignUpDto(
        this.code,
        OAuth2ProviderType.valueOf(this.provider),
        this.nickname,
        this.region1,
        this.region2,
        this.region3,
        GenderType.valueOf(this.gender),
        this.age,
        SkillLevelType.valueOf(this.skillLevel),
        TimePrefrenceType.valueOf(this.preferredWorkoutTime),
        this.favoriteWorkouts);
  }
}
