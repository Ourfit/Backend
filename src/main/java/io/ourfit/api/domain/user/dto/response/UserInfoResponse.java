package io.ourfit.api.domain.user.dto.response;

import io.ourfit.api.domain.user.entity.User;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

/**
 * 사용자 정보 응답 DTO
 *
 * @param id 사용자 ID
 * @param profileUrl 프로필 이미지 URL
 * @param nickname 닉네임
 * @param gender 성별
 * @param age 나이
 * @param skillLevel 운동 실력
 * @param introduction 자기소개
 * @param preferredWorkoutTime 선호 운동 시간
 * @param favoriteWorkout 선호하는 운동
 * @param favoritePlaces 선호하는 운동 시설(장소)
 * @param createdAt 가입일
 * @param nicknameUpdatedAt 닉네임 변경일
 */
@Builder
public record UserInfoResponse(
    long id,
    String profileUrl,
    String nickname,
    String gender,
    int age,
    String skillLevel,
    String introduction,
    String preferredWorkoutTime,
    List<UserFavoriteWorkoutResponse> favoriteWorkout,
    List<UserFavoriteWorkoutPlaceResponse> favoritePlaces,
    String createdAt,
    String nicknameUpdatedAt) {

  public static UserInfoResponse from(User user) {
    return UserInfoResponse.builder()
        .id(user.getId())
        .profileUrl(user.getProfileImageUrl())
        .nickname(user.getNickName())
        .gender(user.getGenderType().name())
        .age(user.getAge())
        .skillLevel(user.getSkillLevelType().name())
        .introduction(user.getIntroduction())
        .preferredWorkoutTime(user.getPreferredWorkoutTime().getDescription())
        .favoriteWorkout(
            StreamUtils.convert(user.getFavoriteWorkouts(), UserFavoriteWorkoutResponse::from))
        .favoritePlaces(
            StreamUtils.convert(
                user.getFavoriteWorkoutPlaces(), UserFavoriteWorkoutPlaceResponse::from))
        .createdAt(user.getCreatedAt().toString())
        .nicknameUpdatedAt(Objects.toString(user.getNickNameUpdatedAt(), null))
        .build();
  }
}
