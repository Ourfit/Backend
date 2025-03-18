package io.ourfit.api.domain.user.controller;

import io.ourfit.api.domain.user.data.dto.internal.MateCandidateSearchDto;
import io.ourfit.api.domain.user.data.dto.request.MateCandidateSearchRequest;
import io.ourfit.api.domain.user.data.dto.response.BasicUserInfoResponse;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.Cursorable;
import io.ourfit.api.global.data.dto.SliceResponse;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v2/users")
public class UserV2Controller {

  private final UserQueryService queryService;

  /** 메이트 관련 사용자 목록 조회 */
  @GetMapping("/mates")
  public ResponseEntity<SliceResponse<BasicUserInfoResponse>> getUsers(
      @Valid MateCandidateSearchRequest request,
      Cursorable cursorable,
      @AuthenticationPrincipal OurfitUserDetails userDetails) {
    var currentUser = userDetails.getUser();
    var contents =
        this.queryService
            .findMateCandidates(
                currentUser, MateCandidateSearchDto.fromRequest(request, currentUser), cursorable)
            .map(BasicUserInfoResponse::from);

    return ResponseEntity.ok(ApiResponse.of(contents, BasicUserInfoResponse::id));
  }
}
