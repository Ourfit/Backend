package io.ourfit.api.domain.auth.controller;

import static io.ourfit.api.domain.auth.data.OAuth2Properties.OAUTH2_REDIRECT_URI;

import io.jsonwebtoken.lang.Assert;
import io.ourfit.api.domain.auth.data.OAuth2Properties;
import io.ourfit.api.domain.auth.template.OAuth2TemplateFactory;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.enums.AccessLevel;
import io.ourfit.api.global.security.data.enums.KeyValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@PublicApi(accessLevel = AccessLevel.PUBLIC, keyValidation = KeyValidation.NONE)
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/{provider}/redirect")
public class OAuthRedirectDispatcher {

  private final OAuth2TemplateFactory templateFactory;
  private final UserQueryService userQueryService;
  private final OAuth2Properties oAuth2Properties;

  @GetMapping
  public ResponseEntity<Void> handleOAuth2Callback(
      @PathVariable String provider, @RequestParam String code) {
    Assert.notNull(code, "Authorization code must not be null");
    final OAuth2ProviderType providerType = OAuth2ProviderType.from(provider);

    final String oAuthId =
        this.templateFactory.getByProviderType(providerType).issueToken(code).getId();
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(
            OAUTH2_REDIRECT_URI.expand(
                this.oAuth2Properties.url(), oAuthId, this.getRegistrationStatus(oAuthId)))
        .build();
  }

  private String getRegistrationStatus(String oAuthId) {
    return this.userQueryService.existsByOAuthId(oAuthId) ? "registered" : "new";
  }
}
