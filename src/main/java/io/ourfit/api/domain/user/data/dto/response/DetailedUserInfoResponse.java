package io.ourfit.api.domain.user.data.dto.response;

import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.List;
import java.util.Objects;
import lombok.Builder;

/**
 * 사용자 정보 응답 DTO
 *
 * @param id 사용자 ID
 * @param oAuthProvider OAuth 제공자
 * @param profileUrl 프로필 이미지 URL
 * @param email 이메일
 * @param nickname 닉네임
 * @param gender 성별
 * @param age 나이
 * @param region1 지역1 (시/도)
 * @param region2 지역2 (시/군/구)
 * @param region3 지역3 (읍/면/동)
 * @param skillLevel 운동 실력
 * @param introduction 자기소개
 * @param openChatUrl 카카오 오픈 채팅 URL
 * @param preferredWorkoutTime 선호 운동 시간
 * @param favoriteWorkouts 선호하는 운동
 * @param favoritePlaces 선호하는 운동 시설(장소)
 * @param createdAt 가입일
 * @param nicknameUpdatedAt 닉네임 변경일
 */
@Builder
public record DetailedUserInfoResponse(
    long id,
    String oAuthProvider,
    String profileUrl,
    String email,
    String nickname,
    String gender,
    int age,
    String region1,
    String region2,
    String region3,
    String skillLevel,
    String introduction,
    String openChatUrl,
    String preferredWorkoutTime,
    List<UserFavoriteWorkoutResponse> favoriteWorkouts,
    List<UserFavoriteWorkoutPlaceResponse> favoritePlaces,
    String createdAt,
    String nicknameUpdatedAt) {

  public static DetailedUserInfoResponse from(User user) {
    return DetailedUserInfoResponse.builder()
        .id(user.getId())
        .oAuthProvider(user.getOAuthProviderType().name())
        .profileUrl(user.getProfileImageUrl())
        .email(user.getEmail())
        .nickname(user.getNickname())
        .gender(user.getGenderType().name())
        .age(user.getAge())
        .region1(user.getRegion1())
        .region2(user.getRegion2())
        .region3(user.getRegion3())
        .skillLevel(user.getSkillLevelType().name())
        .introduction(user.getIntroduction())
        .openChatUrl(user.getOpenChatUrl())
        .preferredWorkoutTime(user.getPreferredWorkoutTime().name())
        .favoriteWorkouts(
            StreamUtils.mapToList(user.getFavoriteWorkouts(), UserFavoriteWorkoutResponse::from))
        .favoritePlaces(
            StreamUtils.mapToList(
                user.getFavoriteWorkoutPlaces(), UserFavoriteWorkoutPlaceResponse::from))
        .createdAt(user.getCreatedAt().toString())
        .nicknameUpdatedAt(Objects.toString(user.getNickNameUpdatedAt(), null))
        .build();
  }
}
