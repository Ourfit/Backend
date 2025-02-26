package io.ourfit.api.domain.auth.controller;

import io.ourfit.api.domain.auth.data.dto.request.TokenIssueRequest;
import io.ourfit.api.domain.auth.data.dto.request.TokenReissueRequest;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.SingleResponse;
import io.ourfit.api.global.jwt.OurfitToken;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
  @PostMapping("/tokens")
  public ResponseEntity<SingleResponse<OurfitToken>> authenticate(
      @RequestBody @Valid final TokenIssueRequest request) {
    OurfitToken ourfitToken = this.authService.issue(request.oAuthId());

    return ResponseEntity.ok((ApiResponse.of(ourfitToken)));
  }

  @PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
  @PostMapping("/tokens/refresh")
  public ResponseEntity<SingleResponse<OurfitToken>> reissue(
      @RequestBody @Valid final TokenReissueRequest request) {
    OurfitToken locatTokenDto =
        this.authService.reissue(request.accessToken(), request.refreshToken());
    return ResponseEntity.ok((ApiResponse.of(locatTokenDto)));
  }

  @DeleteMapping("/tokens")
  public ResponseEntity<Void> revoke(@AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.authService.revoke(userDetails.getUser());
    return ResponseEntity.noContent().build();
  }
}
