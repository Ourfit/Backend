package io.ourfit.api.domain.challenge.controller;

import io.ourfit.api.domain.challenge.data.dto.internal.ChallengeRecordAsDoneDto;
import io.ourfit.api.domain.challenge.data.dto.request.ChallengeRecordAsDoneRequest;
import io.ourfit.api.domain.challenge.data.dto.response.ChallengeRecordResponse;
import io.ourfit.api.domain.challenge.data.entity.ChallengeRecord;
import io.ourfit.api.domain.challenge.service.ChallengeRecordService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import io.ourfit.api.global.utils.StreamUtils;
import jakarta.validation.Valid;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/challenges/{challengeId}/records")
public class ChallengeRecordController {

  private final ChallengeRecordService service;

  @GetMapping("/{yearMonth}")
  public ResponseEntity<BaseResponse<List<ChallengeRecordResponse>>> getChallengeRecord(
      @PathVariable final long challengeId, @PathVariable final String yearMonth) {
    List<ChallengeRecord> response =
        this.service.findMonthlyRecords(challengeId, YearMonth.parse(yearMonth));
    return ResponseEntity.ok(
        BaseResponse.from(StreamUtils.mapToList(response, ChallengeRecordResponse::from)));
  }

  @PostMapping
  public ResponseEntity<Void> markAsDone(
      @PathVariable final long challengeId,
      @RequestBody @Valid ChallengeRecordAsDoneRequest request,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.service.markAsDone(
        challengeId, userDetails.getId(), ChallengeRecordAsDoneDto.from(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
