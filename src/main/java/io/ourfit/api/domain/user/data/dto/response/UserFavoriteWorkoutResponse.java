package io.ourfit.api.domain.user.data.dto.response;

import io.ourfit.api.domain.user.data.dto.internal.UserFavoriteWorkoutDto;
import io.ourfit.api.domain.user.data.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.workout.data.entity.Workout;

/**
 * 사용자 선호하는 운동 종류 응답 DTO
 *
 * @param code 운동 코드
 * @param name 운동 이름
 */
public record UserFavoriteWorkoutResponse(String code, String name) {

  public static UserFavoriteWorkoutResponse from(UserFavoriteWorkout userFavoriteWorkout) {
    Workout workout = userFavoriteWorkout.getWorkout();
    return new UserFavoriteWorkoutResponse(workout.getCode(), workout.getName());
  }

  public static UserFavoriteWorkoutResponse from(UserFavoriteWorkoutDto userFavoriteWorkoutDto) {
    return new UserFavoriteWorkoutResponse(
        userFavoriteWorkoutDto.code(), userFavoriteWorkoutDto.name());
  }
}
