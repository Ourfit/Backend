package io.ourfit.api.domain.user.data.dto.response;

import io.ourfit.api.domain.user.data.dto.internal.UserInfoDto;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.List;
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
 * @param favoriteWorkouts 선호하는 운동
 * @param favoritePlaces 선호하는 운동 시설(장소)
 * @param createdAt 가입일
 * @param nicknameUpdatedAt 닉네임 변경일
 */
@Builder
public record BasicUserInfoResponse(
    long id,
    String profileUrl,
    String nickname,
    String gender,
    int age,
    String skillLevel,
    String introduction,
    String preferredWorkoutTime,
    List<UserFavoriteWorkoutResponse> favoriteWorkouts,
    List<UserFavoriteWorkoutPlaceResponse> favoritePlaces,
    String createdAt,
    String nicknameUpdatedAt) {

  public static BasicUserInfoResponse from(UserInfoDto userInfoDto) {
    return BasicUserInfoResponse.builder()
        .id(userInfoDto.id())
        .profileUrl(userInfoDto.profileUrl())
        .nickname(userInfoDto.nickname())
        .gender(userInfoDto.gender().name())
        .age(userInfoDto.age())
        .skillLevel(userInfoDto.skillLevel().name())
        .introduction(userInfoDto.introduction())
        .preferredWorkoutTime(userInfoDto.preferredWorkoutTime().name())
        .favoriteWorkouts(
            StreamUtils.mapToList(
                userInfoDto.favoriteWorkouts(), UserFavoriteWorkoutResponse::from))
        .build();
  }

  public static BasicUserInfoResponse from(User user) {
    return BasicUserInfoResponse.builder()
        .id(user.getId())
        .profileUrl(user.getProfileImageUrl())
        .gender(user.getGenderType().name())
        .age(user.getAge())
        .introduction(user.getIntroduction())
        .preferredWorkoutTime(user.getPreferredWorkoutTime().name())
        .favoriteWorkouts(
            StreamUtils.mapToList(user.getFavoriteWorkouts(), UserFavoriteWorkoutResponse::from))
        .build();
  }
}
