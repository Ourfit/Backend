package io.ourfit.api.domain.auth.controller;

import static io.ourfit.api.global.security.SecurityConfig.REFRESH_TOKEN_COOKIE_KEY;

import io.ourfit.api.domain.auth.data.dto.request.TokenIssueRequest;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.SingleResponse;
import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.global.jwt.OurfitToken;
import io.ourfit.api.global.jwt.impl.JwtUtils;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import io.ourfit.api.global.utils.ResponseCookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    final var ourfitToken = this.authService.issue(request.oAuthId());
    final var refreshTokenCookie =
        ResponseCookieUtils.refreshTokenCookie(ourfitToken.refreshToken());

    return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
        .body(ApiResponse.of(ourfitToken));
  }

  @PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
  @PostMapping("/tokens/refresh")
  public ResponseEntity<SingleResponse<OurfitToken>> reissue(HttpServletRequest request) {
    var accessToken = JwtUtils.extractToken(request);
    var refreshTokenCookie =
        Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(REFRESH_TOKEN_COOKIE_KEY))
            .findFirst()
            .orElseThrow(AuthenticationException::new);

    var ourfitToken = this.authService.reissue(accessToken, refreshTokenCookie.getValue());
    return createReissueResponse(ourfitToken);
  }

  @DeleteMapping("/tokens")
  public ResponseEntity<Void> revoke(@AuthenticationPrincipal OurfitUserDetails userDetails) {
    this.authService.revoke(userDetails.getUser());
    return ResponseEntity.noContent().build();
  }

  private static ResponseEntity<SingleResponse<OurfitToken>> createReissueResponse(
      OurfitToken ourfitToken) {
    var responseBuilder = ResponseEntity.ok();

    if (ourfitToken.refreshToken() != null) {
      var refreshTokenCookie = ResponseCookieUtils.refreshTokenCookie(ourfitToken.refreshToken());
      return responseBuilder
          .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
          .body(ApiResponse.of(ourfitToken));
    }

    return responseBuilder.body(ApiResponse.of(ourfitToken));
  }
}
