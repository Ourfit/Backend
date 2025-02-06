package io.ourfit.api.domain.challenge.controller;

import io.ourfit.api.domain.challenge.data.dto.internal.NewChallengeRecordDto;
import io.ourfit.api.domain.challenge.data.dto.request.NewChallengeRecordRequest;
import io.ourfit.api.domain.challenge.service.ChallengeRecordService;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/challenges")
public class ChallengeRecordController {

  private final ChallengeRecordService service;

  @PostMapping("/{challengeId}/records")
  public ResponseEntity<Void> createChallengeRecord(
      @PathVariable final long challengeId,
      @RequestBody @Valid NewChallengeRecordRequest request,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.service.create(challengeId, userDetails.getId(), NewChallengeRecordDto.from(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
