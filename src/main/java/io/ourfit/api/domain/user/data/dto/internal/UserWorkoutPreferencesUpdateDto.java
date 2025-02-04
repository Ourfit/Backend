package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkoutPlace;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 사용자 운동 선호 정보 수정 DTO
 *
 * @param preferredWorkoutTime 선호하는 운동 시간
 * @param favoriteWorkouts 선호하는 운동 목록
 * @param favoritePlaces 선호하는 시설(장소) 목록
 */
public record UserWorkoutPreferencesUpdateDto(
    TimePrefrenceType preferredWorkoutTime,
    Set<String> favoriteWorkouts,
    List<UserFavoritePlacesUpsertDto> favoritePlaces) {

  public Set<UserFavoriteWorkoutPlace> toFavoriteWorkoutPlaces() {
    return favoritePlaces.stream()
        .map(place -> UserFavoriteWorkoutPlace.of(null, place))
        .collect(Collectors.toSet());
  }
}
