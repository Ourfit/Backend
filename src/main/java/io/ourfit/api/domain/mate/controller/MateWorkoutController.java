package io.ourfit.api.domain.mate.controller;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutPlacesUpsertDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutTimeUpsertDto;
import io.ourfit.api.domain.mate.data.dto.request.MateWorkoutPlacesUpsertRequest;
import io.ourfit.api.domain.mate.data.dto.request.MateWorkoutTimeUpsertRequest;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/mates/{mateId}/workouts")
public class MateWorkoutController {

  private final MateWorkoutService service;

  /** 메이트와 운동 시설 정보 PUT */
  @PutMapping("/places")
  public ResponseEntity<Void> updateWorkoutPlaces(
      @PathVariable final long mateId, @RequestBody @Valid MateWorkoutPlacesUpsertRequest request) {
    this.service.putWorkoutPlaces(mateId, MateWorkoutPlacesUpsertDto.fromRequest(request));
    return ResponseEntity.ok().build();
  }

  /** 메이트와 운동 시간 정보 PUT */
  @PutMapping("/times")
  public ResponseEntity<Void> updateWorkoutTimes(
      @PathVariable final long mateId, @RequestBody @Valid MateWorkoutTimeUpsertRequest request) {
    this.service.putWorkoutTimes(mateId, MateWorkoutTimeUpsertDto.fromRequest(request));
    return ResponseEntity.ok().build();
  }
}
