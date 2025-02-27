package io.ourfit.api.domain.mate.controller;

import io.ourfit.api.domain.mate.data.dto.response.MateInfoResponse;
import io.ourfit.api.domain.mate.service.MateCommandService;
import io.ourfit.api.domain.mate.service.MateQueryService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.SingleResponse;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/mates")
public class MateController {

  private final MateCommandService commandService;
  private final MateQueryService queryService;

  /** 메이트 신청 */
  @PostMapping("/{receiverId}")
  public ResponseEntity<Void> applyMate(
      @PathVariable final long receiverId, @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.commandService.apply(userDetails.getId(), receiverId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /** 메이트 수락 */
  @PostMapping("/{mateId}/accept")
  public ResponseEntity<Void> acceptMate(
      @PathVariable final long mateId, @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.commandService.accept(userDetails.getId(), mateId);
    return ResponseEntity.ok().build();
  }

  /** 메이트 해제 */
  @DeleteMapping("/{mateId}")
  public ResponseEntity<Void> unmate(
      @PathVariable final long mateId, @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.commandService.unmate(userDetails.getId(), mateId);
    return ResponseEntity.ok().build();
  }

  /** 내 메이트 정보 조회 */
  @GetMapping("/me")
  public ResponseEntity<SingleResponse<MateInfoResponse>> getMyMate(
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    MateInfoResponse response =
        this.queryService
            .findCurrentMateInfo(userDetails.getUser())
            .map(MateInfoResponse::from)
            .orElse(null);

    return ResponseEntity.ok(ApiResponse.of(response));
  }
}
