package io.ourfit.api.domain.mate.controller;

import io.ourfit.api.domain.mate.data.dto.internal.MateHistorySearchDto;
import io.ourfit.api.domain.mate.data.dto.request.MateHistorySearchRequest;
import io.ourfit.api.domain.mate.data.dto.response.MateHistoryResponse;
import io.ourfit.api.domain.mate.service.MateHistoryService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.PageResponse;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/mates")
public class MateHistoryController {

  private final MateHistoryService service;

  /** 나와 관련된 내역 조회 */
  @GetMapping("/me/history")
  public ResponseEntity<PageResponse<MateHistoryResponse>> getMyMateHistories(
      @Valid MateHistorySearchRequest request,
      Pageable pageable,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    Page<MateHistoryResponse> responses =
        this.service
            .findAllByUserId(
                userDetails.getId(), MateHistorySearchDto.fromRequest(request), pageable)
            .map(MateHistoryResponse::from);
    return ResponseEntity.ok(ApiResponse.of(responses));
  }

  /** 특정 알림을 읽음 처리 */
  @PatchMapping("/history/{historyId}/read")
  public ResponseEntity<Void> markAsRead(
      @PathVariable final long historyId, @AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.service.markAsRead(userDetails.getId(), historyId);
    return ResponseEntity.noContent().build();
  }
}
