package io.ourfit.api.domain.workout.controller;

import io.ourfit.api.domain.workout.data.response.WorkoutResponse;
import io.ourfit.api.domain.workout.service.WorkoutService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/workout-types")
public class WorkoutController {

  private final WorkoutService workoutService;

  @GetMapping
  public ResponseEntity<BaseResponse<List<WorkoutResponse>>> getAllWorkoutTypes() {
    List<WorkoutResponse> response =
        StreamUtils.mapToList(this.workoutService.findAll(), WorkoutResponse::from);
    return ResponseEntity.ok(BaseResponse.from(response));
  }
}
