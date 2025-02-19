package io.ourfit.api.global.jwt;

import static io.ourfit.api.global.jwt.impl.JwtProperties.*;

import jakarta.annotation.Nullable;

/**
 * JWT 정보
 *
 * @param grantType 토큰 타입(일반적으로 Bearer)
 * @param accessToken 접근 토큰
 * @param refreshToken 갱신 토큰
 * @param accessTokenExpiresIn 접근 토큰 만료 시간 (s)
 * @param refreshTokenExpiresIn 갱신 토큰 만료 시간 (s)
 */
public record OurfitToken(
    String grantType,
    String accessToken,
    String refreshToken,
    Long accessTokenExpiresIn,
    Long refreshTokenExpiresIn) {

  public static OurfitToken issue(String accessToken, String refreshToken) {
    return new OurfitToken(
        BEARER_PREFIX,
        accessToken,
        refreshToken,
        ACCESS_TOKEN_EXPIRATION.toSeconds(),
        REFRESH_TOKEN_EXPIRATION.toSeconds());
  }

  public static OurfitToken renew(String newAccessToken, @Nullable String newRefreshToken) {
    Long refreshTokenExpiry = newRefreshToken != null ? REFRESH_TOKEN_EXPIRATION.toSeconds() : null;
    return new OurfitToken(
        BEARER_PREFIX,
        newAccessToken,
        newRefreshToken,
        ACCESS_TOKEN_EXPIRATION.toSeconds(),
        refreshTokenExpiry);
  }
}
