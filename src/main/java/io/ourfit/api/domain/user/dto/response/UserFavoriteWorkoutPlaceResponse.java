package io.ourfit.api.domain.user.dto.response;

import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkoutPlace;

/**
 * 사용자 선호하는 운동 시설(장소) 응답 DTO
 *
 * @param placeName 장소 이름
 * @param address 주소
 */
public record UserFavoriteWorkoutPlaceResponse(String placeName, String address) {

  public static UserFavoriteWorkoutPlaceResponse from(
      UserFavoriteWorkoutPlace favoriteWorkoutPlace) {
    return new UserFavoriteWorkoutPlaceResponse(
        favoriteWorkoutPlace.getPlaceName(), favoriteWorkoutPlace.getAddress());
  }
}
