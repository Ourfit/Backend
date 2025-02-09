package io.ourfit.api.domain.auth.template.impl;

import io.ourfit.api.domain.auth.data.OAuth2Properties;
import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.domain.auth.data.entity.OAuth2ProviderToken;
import io.ourfit.api.domain.auth.data.entity.OAuth2ProviderTokenDto;
import io.ourfit.api.domain.auth.template.AbstractOAuth2Template;
import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import io.ourfit.api.infra.client.http.KakaoOAuth2Client;
import io.ourfit.api.infra.client.http.KakaoUserClient;
import io.ourfit.api.infra.redis.OAuth2ProviderTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KakaoOAuth2Template extends AbstractOAuth2Template {

  private final KakaoUserClient kakaoUserClient;
  private final KakaoOAuth2Client kakaoOAuth2Client;

  public KakaoOAuth2Template(
      OAuth2Properties oAuth2Properties,
      OAuth2ProviderTokenRepository providerTokenRepository,
      KakaoUserClient kakaoUserClient,
      KakaoOAuth2Client kakaoOAuth2Client) {
    super(oAuth2Properties, providerTokenRepository);
    this.kakaoUserClient = kakaoUserClient;
    this.kakaoOAuth2Client = kakaoOAuth2Client;
  }

  @Override
  public OAuth2ProviderType getProviderType() {
    return OAuth2ProviderType.KAKAO;
  }

  @Override
  public OAuth2ProviderToken issueToken(final String code) {
    OAuth2Properties.Kakao kakaoProperties = this.oAuth2Properties.kakao();
    OAuth2ProviderTokenDto tokenDto =
        this.kakaoOAuth2Client.issueOrRenewToken(
            OAuth2Properties.Kakao.GRANT_TYPE,
            kakaoProperties.clientId(),
            kakaoProperties.clientSecret(),
            kakaoProperties.redirectUri(),
            code);
    final String oAuthId =
        this.kakaoUserClient.getUserInfo(prependBearer(tokenDto.getAccessToken())).getId();
    return super.providerTokenRepository.save(
        OAuth2ProviderToken.from(OAuth2ProviderType.KAKAO, oAuthId, tokenDto));
  }

  @Override
  public OAuth2UserInfo getUserInfo(final String oAuthId) {
    final String accessToken = super.findToken(oAuthId).getAccessToken();
    return this.kakaoUserClient.getUserInfo(prependBearer(accessToken));
  }

  @Override
  @Transactional
  public void withdrawal(final String oAuthId) {
    final String accessToken = super.findToken(oAuthId).getAccessToken();
    this.kakaoUserClient.withdrawal(prependBearer(accessToken));
    this.providerTokenRepository.deleteById(oAuthId);
  }
}
