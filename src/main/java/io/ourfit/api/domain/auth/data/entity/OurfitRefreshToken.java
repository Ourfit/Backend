package io.ourfit.api.domain.auth.data.entity;

import static io.ourfit.api.global.jwt.impl.JwtProperties.REFRESH_TOKEN_RENEWAL_THRESHOLD;

import io.ourfit.api.global.jwt.OurfitToken;
import jakarta.persistence.Id;
import java.time.Duration;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Builder
@RedisHash("OURFIT_REFRESH_TOKEN")
public class OurfitRefreshToken {

  @Id private Long id;

  private String refreshToken;

  private Long issuedAt;

  @TimeToLive private Long refreshTokenExpiresIn;

  public static OurfitRefreshToken from(final long id, OurfitToken token) {
    return OurfitRefreshToken.builder()
        .id(id)
        .refreshToken(token.refreshToken())
        .issuedAt(Instant.now().getEpochSecond())
        .refreshTokenExpiresIn(token.refreshTokenExpiresIn())
        .build();
  }

  public boolean matches(final String refreshToken) {
    return this.refreshToken.equals(refreshToken);
  }

  public boolean isExpiringSoon() {
    Instant now = Instant.now();
    Instant expirationTime =
        Instant.ofEpochSecond(this.issuedAt).plusSeconds(this.refreshTokenExpiresIn);
    return Duration.between(now, expirationTime).compareTo(REFRESH_TOKEN_RENEWAL_THRESHOLD) <= 0;
  }
}
