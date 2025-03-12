package io.ourfit.api.domain.auth.controller;

import static io.ourfit.api.domain.auth.data.OAuth2Properties.OAUTH2_REDIRECT_URI;

import io.ourfit.api.domain.auth.data.OAuth2Properties;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.domain.auth.template.OAuth2ProfileContextHolder;
import io.ourfit.api.domain.auth.template.OAuth2TemplateFactory;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.custom.InvalidParameterException;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
@RestController
@RequiredArgsConstructor
// @RequestMapping("/v1/oauth2/{provider}/redirect")
public class OAuth2RedirectDispatcher {

  private final OAuth2TemplateFactory templateFactory;
  private final OAuth2Properties oAuth2Properties;
  private final AuthService authService;
  private final UserQueryService userQueryService;

  @GetMapping("/v1/oauth2/{provider}/redirect")
  public ResponseEntity<Void> handleOAuth2Callback(
      @PathVariable String provider, @RequestParam @NotEmpty String code) {
    final var providerType =
        OAuth2ProviderType.findByName(provider).orElseThrow(InvalidParameterException::new);
    OAuth2ProfileContextHolder.setAsProduction();
    return this.doHandleInternal(providerType, code, this.oAuth2Properties.url());
  }

  @GetMapping("/v1-dev/oauth2/{provider}/redirect")
  public ResponseEntity<Void> handleDevelopOAuth2Callback(
      @PathVariable String provider, @RequestParam @NotEmpty String code) {
    final var providerType =
        OAuth2ProviderType.findByName(provider).orElseThrow(InvalidParameterException::new);
    OAuth2ProfileContextHolder.setAsDevelop();
    return this.doHandleInternal(providerType, code, "http://localhost:3000");
  }

  private ResponseEntity<Void> doHandleInternal(
      OAuth2ProviderType provider, String code, String redirectUri) {
    String oAuthId = this.templateFactory.getByProviderType(provider).issueToken(code).getId();
    String authCode = this.authService.issueAuthCode(oAuthId).getCode();
    OAuth2ProfileContextHolder.clear();
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(
            OAUTH2_REDIRECT_URI.expand(
                redirectUri, oAuthId, authCode, this.getRegistrationStatus(oAuthId)))
        .build();
  }

  private String getRegistrationStatus(String oAuthId) {
    return this.userQueryService.existsByOAuthId(oAuthId) ? "registered" : "new";
  }
}
