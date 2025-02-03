package io.ourfit.api.domain.user.dto.internal;

import io.ourfit.api.domain.user.entity.enums.GenderType;
import io.ourfit.api.domain.user.entity.enums.SkillLevelType;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import java.util.List;

/**
 * 사용자 정보 DTO
 *
 * @param id 사용자 ID
 * @param profileUrl 프로필 이미지 URL
 * @param nickname 닉네임
 * @param gender 성별
 * @param age 나이
 * @param skillLevel 운동 실력
 * @param introduction 자기소개
 * @param preferredWorkoutTime 선호 운동 시간
 * @param favoriteWorkouts 선호하는 운동
 */
public record UserInfoDto(
    long id,
    String profileUrl,
    String nickname,
    GenderType gender,
    int age,
    SkillLevelType skillLevel,
    String introduction,
    TimePrefrenceType preferredWorkoutTime,
    List<UserFavoriteWorkoutDto> favoriteWorkouts) {}
