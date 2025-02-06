package io.ourfit.api.domain.user.data.dto.request;

import io.ourfit.api.domain.user.data.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.workout.data.enums.TimePrefrenceType;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.global.web.validation.Enumerable;
import java.util.List;
import java.util.Set;

/**
 * 사용자 운동 선호 정보 수정 요청 DTO
 *
 * @param preferredWorkoutTime 선호 운동 시간
 * @param favoriteWorkouts 선호하는 운동 목록
 * @param favoritePlaces 선호하는 시설(장소) 목록
 */
public record UserWorkoutPreferencesUpdateRequest(
    @Enumerable(type = TimePrefrenceType.class, required = false) String preferredWorkoutTime,
    Set<String> favoriteWorkouts,
    List<UserFavoritePlacesUpsertRequest> favoritePlaces) {

  public UserWorkoutPreferencesUpdateDto toDto() {
    return new UserWorkoutPreferencesUpdateDto(
        TimePrefrenceType.valueOf(this.preferredWorkoutTime),
        this.favoriteWorkouts,
        StreamUtils.mapToList(this.favoritePlaces, UserFavoritePlacesUpsertRequest::toDto));
  }
}
