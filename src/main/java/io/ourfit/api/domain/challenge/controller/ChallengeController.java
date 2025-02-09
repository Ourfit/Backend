package io.ourfit.api.domain.challenge.controller;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeCreateDto;
import io.ourfit.api.domain.challenge.data.dto.request.ChallengeCreateRequest;
import io.ourfit.api.domain.challenge.data.dto.request.ChallengeUpdateRequest;
import io.ourfit.api.domain.challenge.data.dto.response.MyChallengeInfoResponse;
import io.ourfit.api.domain.challenge.service.ChallengeService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.exception.custom.InvalidParameterException;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import io.ourfit.api.global.utils.StreamUtils;
import jakarta.validation.Valid;
import java.time.DayOfWeek;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/challenges")
public class ChallengeController {

  private final ChallengeService service;

  /** 현재 진행 중인 내 챌린지 조회 */
  @GetMapping("/me")
  public ResponseEntity<BaseResponse<MyChallengeInfoResponse>> getCurrentChallengeRecord(
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    MyChallengeInfoResponse response =
        this.service
            .findByUserIdWithRecords(userDetails.getId())
            .map(MyChallengeInfoResponse::from)
            .orElse(null);

    return ResponseEntity.ok(BaseResponse.from(response));
  }

  /** 챌린지 등록 */
  @PostMapping
  public ResponseEntity<Void> createChallenge(
      @RequestBody @Valid ChallengeCreateRequest request,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    if (request.isGoalSettingInValid()) {
      throw new InvalidParameterException("Challenge goal setting is invalid");
    }
    this.service.create(userDetails.getId(), ChallengeCreateDto.fromRequest(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** 챌린지 목표 요일 수정 */
  @PatchMapping("/{challengeId}")
  public ResponseEntity<Void> updateGoalDayOfWeeks(
      @PathVariable long challengeId, @RequestBody @Valid ChallengeUpdateRequest request) {
    this.service.updateGoalDayOfWeeks(
        challengeId, StreamUtils.mapToSet(request.goalWorkoutDayOfWeeks(), DayOfWeek::valueOf));
    return ResponseEntity.ok().build();
  }

  /** 챌린지 삭제 */
  @DeleteMapping("/{challengeId}")
  public ResponseEntity<Void> deleteChallenge(@PathVariable long challengeId) {
    this.service.delete(challengeId);
    return ResponseEntity.ok().build();
  }
}
