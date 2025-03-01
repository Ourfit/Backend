package io.ourfit.api.domain.auth.service.impl;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.auth.data.entity.OurfitAuthCode;
import io.ourfit.api.domain.auth.service.AuthService;
import io.ourfit.api.domain.auth.service.OAuth2Service;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.jwt.OurfitToken;
import io.ourfit.api.infra.redis.OurfitAuthCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final OurfitAuthCodeRepository authCodeRepository;
  private final OAuth2Service oAuth2Service;
  private final UserQueryService userQueryService;
  private final JwtProvider jwtProvider;

  @Override
  public OurfitAuthCode issueAuthCode(String oAuthId) {
    return this.authCodeRepository.save(OurfitAuthCode.from(oAuthId));
  }

  @Override
  public OurfitToken issueToken(final String oAuthId, final String authCode) {
    this.consumeAuthCode(oAuthId, authCode);

    OAuth2UserInfo oAuth2UserInfo = this.oAuth2Service.getUserInfo(oAuthId);
    return this.userQueryService
        .findByOAuthId(oAuth2UserInfo.getId())
        .map(this.jwtProvider::create)
        .orElseThrow(() -> new NoSuchEntityException(ApiExceptionType.NOT_FOUND_USER));
  }

  @Override
  public OurfitToken reissueToken(String accessToken, String refreshToken) {
    return this.jwtProvider.reissue(accessToken, refreshToken);
  }

  @Override
  public void consumeAuthCode(final String oAuthId, final String inputCode) {
    this.authCodeRepository
        .findById(oAuthId)
        .map(
            storedCode -> {
              this.authCodeRepository.delete(storedCode);
              if (storedCode.doesNotMatch(inputCode)) {
                throw new AuthenticationException();
              }
              return storedCode;
            })
        .orElseThrow(AuthenticationException::new);
  }

  @Override
  public void revokeToken(User user) {
    this.jwtProvider.revoke(user);
  }
}
