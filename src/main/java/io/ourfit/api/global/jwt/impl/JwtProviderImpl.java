package io.ourfit.api.global.jwt.impl;

import static io.ourfit.api.global.jwt.impl.JwtProperties.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.ourfit.api.domain.auth.data.entity.OurfitRefreshToken;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.jwt.OurfitToken;
import io.ourfit.api.global.jwt.TokenException;
import io.ourfit.api.infra.redis.OurfitRefreshTokenRepository;
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
  private final OurfitRefreshTokenRepository refreshTokenRepository;

  @Override
  public OurfitToken create(User user) {
    final var now = this.getCurrentTime();
    final var ourfitToken =
        OurfitToken.issue(
            this.createAccessToken(user, now), this.createRefreshToken(user.getId(), now));

    this.refreshTokenRepository.save(OurfitRefreshToken.from(user.getId(), ourfitToken));
    return ourfitToken;
  }

  @Override
  public OurfitToken reissue(String oldAccessToken, String refreshToken) {
    if (this.parse(refreshToken).isEmpty()) {
      throw new TokenException();
    }
    final var claims = this.parse(oldAccessToken).orElseThrow(TokenException::new);
    final var userId = Long.parseLong(claims.getSubject());

    return this.refreshTokenRepository
        .findById(userId)
        .filter(token -> token.matches(refreshToken))
        .map(token -> this.doRenew(userId, claims, token))
        .orElseThrow(TokenException::new);
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

  @Override
  public void revoke(User user) {
    this.refreshTokenRepository.deleteById(user.getId());
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

  private OurfitToken doRenew(final long userId, Claims claims, OurfitRefreshToken refreshToken) {
    final var accessToken = this.createAccessToken(claims, this.getCurrentTime());
    final var refreshTokenExpiresIn =
        refreshToken.isExpiringSoon()
            ? this.createRefreshToken(userId, this.getCurrentTime())
            : null;

    return OurfitToken.reissue(accessToken, refreshTokenExpiresIn);
  }

  private Date toDate(Duration duration) {
    return Date.from(Instant.now(this.clock).plus(duration));
  }

  private Date getCurrentTime() {
    return Date.from(Instant.now(this.clock));
  }
}
