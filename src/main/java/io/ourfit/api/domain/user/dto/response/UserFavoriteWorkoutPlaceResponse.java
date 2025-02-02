package io.ourfit.api.domain.user.dto.response;

import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkoutPlace;

public record UserFavoriteWorkoutPlaceResponse(String placeName, String address) {

  public static UserFavoriteWorkoutPlaceResponse from(
      UserFavoriteWorkoutPlace favoriteWorkoutPlace) {
    return new UserFavoriteWorkoutPlaceResponse(
        favoriteWorkoutPlace.getPlaceName(), favoriteWorkoutPlace.getAddress());
  }
}
