package io.ourfit.api.domain.challenge.controller;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeDto;
import io.ourfit.api.domain.challenge.data.dto.request.ChallengeUpdateRequest;
import io.ourfit.api.domain.challenge.data.dto.request.NewChallengeRequest;
import io.ourfit.api.domain.challenge.service.ChallengeService;
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

  /** 챌린지 등록 */
  @PostMapping
  public ResponseEntity<Void> createChallenge(
      @RequestBody @Valid NewChallengeRequest request,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    if (request.isGoalSettingInValid()) {
      throw new InvalidParameterException("Challenge goal setting is invalid");
    }
    this.service.create(userDetails.getId(), NewChallengeDto.fromRequest(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** 챌린지 목표 요일 수정 */
  @PatchMapping("/{challengeId}/goal-day-of-weeks")
  public ResponseEntity<Void> updateGoalDayOfWeeks(
      @PathVariable long challengeId, @RequestBody @Valid ChallengeUpdateRequest request) {
    this.service.updateGoalDayOfWeeks(
        challengeId, StreamUtils.mapToSet(request.goalDayOfWeeks(), DayOfWeek::valueOf));
    return ResponseEntity.ok().build();
  }
}
