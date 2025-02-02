package io.ourfit.api.domain.user.dto.response;

import io.ourfit.api.domain.user.entity.association.UserFavoriteWorkout;
import io.ourfit.api.domain.workout.Workout;

public record UserFavoriteWorkoutResponse(String code, String name) {

  public static UserFavoriteWorkoutResponse from(UserFavoriteWorkout userFavoriteWorkout) {
    Workout workout = userFavoriteWorkout.getWorkout();
    return new UserFavoriteWorkoutResponse(workout.getCode(), workout.getName());
  }
}
