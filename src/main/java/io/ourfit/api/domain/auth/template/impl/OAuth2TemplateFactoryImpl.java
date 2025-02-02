package io.ourfit.api.domain.auth.template.impl;

import io.ourfit.api.domain.auth.data.entity.OAuth2ProviderToken;
import io.ourfit.api.domain.auth.template.OAuth2Template;
import io.ourfit.api.domain.auth.template.OAuth2TemplateFactory;
import io.ourfit.api.domain.user.entity.enums.OAuth2ProviderType;
import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.infra.redis.OAuth2ProviderTokenRepository;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OAuth2TemplateFactoryImpl implements OAuth2TemplateFactory {

  private final Map<OAuth2ProviderType, OAuth2Template> oAuth2TemplateMap;
  private final OAuth2ProviderTokenRepository providerTokenRepository;

  /** 불변 EnumMap을 생성해 OAuth2Template을 관리 */
  public OAuth2TemplateFactoryImpl(
      OAuth2ProviderTokenRepository providerTokenRepository,
      KakaoOAuth2Template kakaoOAuth2Template) {
    EnumMap<OAuth2ProviderType, OAuth2Template> map = new EnumMap<>(OAuth2ProviderType.class);
    map.put(OAuth2ProviderType.KAKAO, kakaoOAuth2Template);
    this.oAuth2TemplateMap = Collections.unmodifiableMap(map);
    this.providerTokenRepository = providerTokenRepository;
  }

  /**
   * {@inheritDoc}
   *
   * @param oAuth2ProviderType OAuth2ProviderType
   * @return 선택된 OAuth2Template
   */
  @Override
  public OAuth2Template getByProviderType(final OAuth2ProviderType oAuth2ProviderType) {
    return this.oAuth2TemplateMap.get(oAuth2ProviderType);
  }

  /**
   * {@inheritDoc}
   *
   * @param oAuthId OAuth2ProviderToken의 id
   * @return 선택된 OAuth2Template
   * @throws AuthenticationException OAuth2ProviderToken이 없을 경우
   */
  @Override
  public OAuth2Template getByOAuthId(final String oAuthId) {
    return this.providerTokenRepository
        .findById(oAuthId)
        .map(OAuth2ProviderToken::getProviderType)
        .map(this::getByProviderType)
        .orElseThrow(AuthenticationException::new);
  }
}
