package io.ourfit.api.domain.user.dto.request;

import io.ourfit.api.domain.user.dto.internal.UserWorkoutPreferencesUpdateDto;
import io.ourfit.api.domain.workout.enums.TimePrefrenceType;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.global.validation.Enumerable;
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
    @Enumerable(targetClass = TimePrefrenceType.class, required = false)
        String preferredWorkoutTime,
    Set<String> favoriteWorkouts,
    List<UserFavoritePlacesUpsertRequest> favoritePlaces) {

  public UserWorkoutPreferencesUpdateDto toDto() {
    return new UserWorkoutPreferencesUpdateDto(
        TimePrefrenceType.valueOf(this.preferredWorkoutTime),
        this.favoriteWorkouts,
        StreamUtils.convert(this.favoritePlaces, UserFavoritePlacesUpsertRequest::toDto));
  }
}
