package io.ourfit.api.global.jwt.impl;

import static io.ourfit.api.global.jwt.impl.JwtProperties.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.jwt.OurfitToken;
import io.ourfit.api.global.jwt.TokenException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtProviderImpl implements JwtProvider {

  private final Clock clock;
  private final JwtProperties jwtProperties;

  @Override
  public OurfitToken create(User user) {

    final Date now = this.getCurrentTime();
    final String accessToken = this.createAccessToken(user, now);
    final String refreshToken = this.createRefreshToken(user.getId(), now);

    return OurfitToken.builder()
        .grantType(BEARER_PREFIX)
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toSeconds())
        .refreshTokenExpiresIn(REFRESH_TOKEN_EXPIRATION.toSeconds())
        .build();
  }

  @Override
  public OurfitToken renew(String oldAccessToken, String refreshToken) {
    Claims claims = this.parse(refreshToken).orElseThrow(TokenException::new);

    // Validations here...

    return OurfitToken.builder()
        .accessToken(this.createAccessToken(claims, this.getCurrentTime()))
        .accessTokenExpiresIn(ACCESS_TOKEN_EXPIRATION.toSeconds())
        .build();
  }

  @Override
  public Optional<Claims> parse(String token) {
    try {
      Claims claims =
          Jwts.parser()
              .requireIssuer(this.jwtProperties.issuer())
              .verifyWith(this.jwtProperties.key())
              .build()
              .parseSignedClaims(token)
              .getPayload();
      return Optional.of(claims);
    } catch (ExpiredJwtException ex) {
      return Optional.of(ex.getClaims());
    } catch (JwtException ex) {
      log.debug("Could not parse JWT Claims. / Reason: {}", ex.getMessage());
      return Optional.empty();
    }
  }

  private String createAccessToken(User user, Date now) {
    return this.createAccessToken(user.getId().toString(), user.getRoleType().name(), now);
  }

  private String createAccessToken(Claims claims, Date now) {
    return this.createAccessToken(
        claims.getSubject(), claims.get(AUTHENTICATION_KEY, String.class), now);
  }

  private String createAccessToken(String userId, String claim, Date now) {
    return Jwts.builder()
        .issuer(this.jwtProperties.issuer())
        .subject(userId)
        .issuedAt(now)
        .notBefore(now)
        .claim(AUTHENTICATION_KEY, claim)
        .expiration(this.toDate(ACCESS_TOKEN_EXPIRATION))
        .signWith(this.jwtProperties.key())
        .compact();
  }

  private String createRefreshToken(final long userId, Date now) {
    return Jwts.builder()
        .issuer(this.jwtProperties.issuer())
        .subject(Long.toString(userId))
        .issuedAt(now)
        .notBefore(now)
        .expiration(this.toDate(REFRESH_TOKEN_EXPIRATION))
        .signWith(this.jwtProperties.key())
        .compact();
  }

  private Date toDate(Duration duration) {
    return Date.from(Instant.now(this.clock).plus(duration));
  }

  private Date getCurrentTime() {
    return Date.from(Instant.now(this.clock));
  }
}
