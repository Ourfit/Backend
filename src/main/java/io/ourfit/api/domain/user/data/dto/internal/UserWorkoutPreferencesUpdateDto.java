package io.ourfit.api.domain.user.data.dto.internal;

import io.ourfit.api.domain.user.data.dto.request.UserWorkoutPreferencesUpdateRequest;
import io.ourfit.api.domain.workout.data.enums.TimePrefrenceType;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.List;
import java.util.Set;

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

  public static UserWorkoutPreferencesUpdateDto fromRequest(
      UserWorkoutPreferencesUpdateRequest request) {
    return new UserWorkoutPreferencesUpdateDto(
        TimePrefrenceType.valueOf(request.preferredWorkoutTime()),
        request.favoriteWorkouts(),
        StreamUtils.mapToList(request.favoritePlaces(), UserFavoritePlacesUpsertDto::fromRequest));
  }
}
