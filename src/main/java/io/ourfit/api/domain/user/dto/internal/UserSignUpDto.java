package io.ourfit.api.domain.user.dto.internal;

import io.ourfit.api.domain.user.entity.enums.GenderType;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.entity.enums.SkillLevelType;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import java.util.Set;

/**
 * 사용자 회원가입 처리 DTO
 *
 * @param code OAuth2 인가 코드
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
    Set<String> favoriteWorkouts) {}
