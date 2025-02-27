package io.ourfit.api.domain.auth.template;

import static io.ourfit.api.global.jwt.impl.JwtProperties.BEARER_PREFIX;

import io.ourfit.api.domain.auth.data.OAuth2Properties;
import io.ourfit.api.domain.auth.data.entity.OAuth2ProviderToken;
import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.infra.redis.OAuth2ProviderTokenRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public abstract class AbstractOAuth2Template implements OAuth2Template {

  protected static final Logger log = LoggerFactory.getLogger(AbstractOAuth2Template.class);

  protected final OAuth2Properties oAuth2Properties;
  protected final OAuth2ProviderTokenRepository providerTokenRepository;

  @Override
  public boolean isAuthenticated(final String oAuthId) {
    return this.providerTokenRepository.existsById(oAuthId);
  }

  /**
   * 주어진 OAuth2 ID에 대한 유효한 인증 토큰을 반환한다.
   *
   * <p>- 캐시된 접근 토큰이 만료되지 않았다면 그대로 반환한다. <br>
   * - 만료된 경우, 접근 토큰을 갱신하고 갱신된 토큰을 반환한다.
   *
   * @param oAuthId 사용자의 OAuth2 ID
   * @return 유효한 {@link OAuth2ProviderToken} 객체
   * @throws AuthenticationException 사용자의 OAuth2 인증 토큰이 존재하지 않을 경우
   */
  protected OAuth2ProviderToken getValidToken(final String oAuthId) {
    OAuth2ProviderToken token = this.findToken(oAuthId);
    return token.isAccessTokenExpired() ? this.renewToken(oAuthId) : token;
  }

  /**
   * 사용자의 OAuth2 인증 토큰 정보를 조회한다.
   *
   * @param oAuthId 조회에 사용할 사용자의 OAuth2 ID
   * @return 사용자의 OAuth2 인증 토큰 정보
   * @throws AuthenticationException 사용자의 OAuth2 인증 토큰 정보가 존재하지 않을 경우
   */
  protected OAuth2ProviderToken findToken(final String oAuthId) {
    return this.providerTokenRepository.findById(oAuthId).orElseThrow(AuthenticationException::new);
  }

  /**
   * OAuth2 인증 토큰에 {@code Bearer} 접두어를 추가한다.
   *
   * @param token 접두어를 추가할 토큰
   * @return {@code Bearer} 접두어가 추가된 토큰
   */
  protected static String prependBearer(final String token) {
    return BEARER_PREFIX.concat(token);
  }
}
