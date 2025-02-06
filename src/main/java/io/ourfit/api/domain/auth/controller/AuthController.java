package io.ourfit.api.domain.auth.controller;

import io.ourfit.api.domain.auth.data.dto.request.TokenIssueRequest;
import io.ourfit.api.domain.auth.data.dto.request.TokenRenewRequest;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.jwt.OurfitToken;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PublicApi
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/tokens")
  public ResponseEntity<BaseResponse<OurfitToken>> authenticate(
      @RequestBody @Valid final TokenIssueRequest request) {
    OurfitToken ourfitToken = this.authService.issue(request.oAuthId());

    return ResponseEntity.ok((BaseResponse.from(ourfitToken)));
  }

  @PostMapping("/tokens/refresh")
  public ResponseEntity<BaseResponse<OurfitToken>> renewToken(
      @RequestBody final TokenRenewRequest request) {
    OurfitToken locatTokenDto =
        this.authService.renew(request.accessToken(), request.refreshToken());
    return ResponseEntity.ok((BaseResponse.from(locatTokenDto)));
  }
}
