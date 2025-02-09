package io.ourfit.api.domain.workout.data.response;

import io.ourfit.api.domain.workout.data.entity.Workout;

/**
 * 운동 종목 응답 DTO
 *
 * @param code 운동 종목 코드
 * @param name 운동 종목 이름
 */
public record WorkoutResponse(String code, String name) {

  public static WorkoutResponse from(Workout workout) {
    return new WorkoutResponse(workout.getCode(), workout.getName());
  }
}
