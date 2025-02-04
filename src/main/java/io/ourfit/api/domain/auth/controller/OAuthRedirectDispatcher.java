package io.ourfit.api.domain.auth.controller;

import io.jsonwebtoken.lang.Assert;
import io.ourfit.api.domain.auth.template.OAuth2TemplateFactory;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.global.data.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/oauth2/{provider}/redirect")
public class OAuthRedirectDispatcher {

  private static final UriTemplate REDIRECT_URI =
      new UriTemplate("{clientUrl}?oAuthId={oAuthId}&status={status}");

  private static final String STATUS_REGISTERED = "registered";
  private static final String STATUS_NEW = "new";

  private final OAuth2TemplateFactory templateFactory;
  private final UserService userService;

  @GetMapping
  public ResponseEntity<BaseResponse<Void>> handleOAuth2Callback(
      @PathVariable String provider, @RequestParam String code) {
    Assert.notNull(code, "Authorization code must not be null");
    final OAuth2ProviderType providerType = OAuth2ProviderType.from(provider);

    final String oAuthId =
        this.templateFactory.getByProviderType(providerType).issueToken(code).getId();
    return ResponseEntity.status(HttpStatus.FOUND)
        .location(
            REDIRECT_URI.expand(
                "https://client.ourfit.io", oAuthId, this.getRegistrationStatus(oAuthId)))
        .build();
  }

  private String getRegistrationStatus(String oAuthId) {
    return this.userService.existsByOAuthId(oAuthId) ? STATUS_REGISTERED : STATUS_NEW;
  }
}
