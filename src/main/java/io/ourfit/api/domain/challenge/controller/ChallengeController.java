package io.ourfit.api.domain.challenge.controller;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeCreateDto;
import io.ourfit.api.domain.challenge.data.dto.request.ChallengeCreateRequest;
import io.ourfit.api.domain.challenge.data.dto.request.ChallengeUpdateRequest;
import io.ourfit.api.domain.challenge.data.dto.response.ChallengeInfoResponse;
import io.ourfit.api.domain.challenge.data.entity.Challenge;
import io.ourfit.api.domain.challenge.service.ChallengeService;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.SingleResponse;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.InvalidParameterException;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import io.ourfit.api.global.utils.StreamUtils;
import jakarta.validation.Valid;
import java.time.DayOfWeek;
import java.util.List;
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
  public ResponseEntity<SingleResponse<ChallengeInfoResponse>> getCurrentChallengeRecord(
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    User currentUser = userDetails.getUser();

    List<Challenge> result = this.service.findAllByUser(currentUser);
    var response = ChallengeInfoResponse.from(currentUser, result);

    return ResponseEntity.ok(ApiResponse.of(response));
  }

  /** 챌린지 등록 */
  @PostMapping
  public ResponseEntity<Void> createChallenge(
      @RequestBody @Valid ChallengeCreateRequest request,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    if (request.isDateSettingInValid()) {
      throw new InvalidParameterException(ApiExceptionType.INVALID_DATE_RANGE);
    }
    this.service.create(userDetails.getId(), ChallengeCreateDto.fromRequest(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** 챌린지 목표 요일 수정 */
  @PatchMapping("/{challengeId}")
  public ResponseEntity<Void> updateGoalDayOfWeeks(
      @PathVariable long challengeId, @RequestBody @Valid ChallengeUpdateRequest request) {
    this.service.setGoalDayOfWeeks(
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
