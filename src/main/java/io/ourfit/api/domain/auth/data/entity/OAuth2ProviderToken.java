package io.ourfit.api.domain.auth.data.entity;

import io.ourfit.api.domain.user.data.entity.enums.OAuth2ProviderType;
import jakarta.persistence.Id;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("OAUTH2_PROVIDER_TOKEN")
public class OAuth2ProviderToken {

  @Id private String id;

  private OAuth2ProviderType providerType;

  private String accessToken;

  private String refreshToken;

  private String idToken;

  private Integer accessTokenExpiresIn;

  private Integer refreshTokenExpiresIn;

  private Long issuedAt;

  @TimeToLive private Long timeToLive;

  public static OAuth2ProviderToken from(
      OAuth2ProviderType providerType, String oAuthId, OAuth2ProviderTokenDto providerTokenDto) {
    return builder()
        .id(oAuthId)
        .providerType(providerType)
        .idToken(providerTokenDto.getIdToken())
        .accessToken(providerTokenDto.getAccessToken())
        .accessTokenExpiresIn(providerTokenDto.getAccessTokenExpiresIn())
        .refreshToken(providerTokenDto.getRefreshToken())
        .refreshTokenExpiresIn(providerTokenDto.getRefreshTokenExpiresIn())
        .issuedAt(Instant.now().getEpochSecond())
        .timeToLive(providerTokenDto.getRefreshTokenExpiresIn().longValue())
        .build();
  }

  public OAuth2ProviderToken renew(OAuth2ProviderTokenDto providerTokenDto) {
    this.accessToken = providerTokenDto.getAccessToken();
    this.accessTokenExpiresIn = providerTokenDto.getAccessTokenExpiresIn();
    if (providerTokenDto.getRefreshToken() != null) {
      this.refreshToken = providerTokenDto.getRefreshToken();
      this.refreshTokenExpiresIn = providerTokenDto.getRefreshTokenExpiresIn();
      this.issuedAt = Instant.now().getEpochSecond();
    }
    return this;
  }

  public boolean isAccessTokenExpired() {
    Instant expirationTime =
        Instant.ofEpochSecond(this.issuedAt).plusSeconds(this.accessTokenExpiresIn);
    return Instant.now().isAfter(expirationTime);
  }
}
