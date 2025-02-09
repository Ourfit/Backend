package io.ourfit.api.domain.auth.service.impl;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.jwt.OurfitToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final OAuth2Service oAuth2Service;
  private final UserQueryService userQueryService;
  private final JwtProvider jwtProvider;

  @Override
  public OurfitToken issue(final String oAuthId) {
    OAuth2UserInfo oAuth2UserInfo = this.oAuth2Service.getUserInfo(oAuthId);

    return this.userQueryService
        .findByOAuthId(oAuth2UserInfo.getId())
        .map(this.jwtProvider::create)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }

  @Override
  public OurfitToken renew(String accessToken, String refreshToken) {
    return this.jwtProvider.renew(accessToken, refreshToken);
  }
}
