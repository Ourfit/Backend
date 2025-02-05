package io.ourfit.api.domain.mate.controller;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutPlacesUpsertDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutTimeUpsertDto;
import io.ourfit.api.domain.mate.data.dto.request.MateWorkoutPlacesUpsertRequest;
import io.ourfit.api.domain.mate.data.dto.request.MateWorkoutTimeUpsertRequest;
import io.ourfit.api.domain.mate.service.MateService;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/mates")
public class MateController {

  private final MateService mateService;
  private final MateWorkoutService mateWorkoutService;

  /** 메이트 신청 */
  @PostMapping("/{requesteeId}")
  public ResponseEntity<Void> applyMate(
      @PathVariable final long requesteeId,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.mateService.apply(userDetails.getId(), requesteeId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** 메이트 수락 */
  @PostMapping("/{mateId}/accept")
  public ResponseEntity<Void> acceptMate(@PathVariable final long mateId) {
    this.mateService.accept(mateId);
    return ResponseEntity.ok().build();
  }

  /** 메이트 해제 */
  @DeleteMapping("/{mateId}")
  public ResponseEntity<Void> deleteMate(@PathVariable final long mateId) {
    this.mateService.delete(mateId);
    return ResponseEntity.ok().build();
  }

  /** 내 메이트 정보 조회 */
  @GetMapping("/me")
  public ResponseEntity<Void> getMyMate(@AuthenticationPrincipal OurfitUserDetails userDetails) {
    return ResponseEntity.ok().build();
  }

  /** 메이트와 운동 시설 정보 PUT */
  @PutMapping("/{mateId}/workout-places")
  public ResponseEntity<Void> updateWorkoutPlaces(
      @PathVariable final long mateId, @RequestBody @Valid MateWorkoutPlacesUpsertRequest request) {
    this.mateWorkoutService.putWorkoutPlaces(
        mateId, MateWorkoutPlacesUpsertDto.fromRequest(request));
    return ResponseEntity.ok().build();
  }

  /** 메이트와 운동 시간 정보 PUT */
  @PutMapping("/{mateId}/workout-times")
  public ResponseEntity<Void> updateWorkoutTimes(
      @PathVariable final long mateId, @RequestBody @Valid MateWorkoutTimeUpsertRequest request) {
    this.mateWorkoutService.putWorkoutTimes(mateId, MateWorkoutTimeUpsertDto.fromRequest(request));
    return ResponseEntity.ok().build();
  }
}
