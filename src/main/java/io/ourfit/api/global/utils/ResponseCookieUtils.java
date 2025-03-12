package io.ourfit.api.global.utils;

import static io.ourfit.api.global.jwt.impl.JwtProperties.REFRESH_TOKEN_EXPIRATION;
import static io.ourfit.api.global.security.SecurityConfig.REFRESH_TOKEN_COOKIE_KEY;

import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.http.ResponseCookie;

public final class ResponseCookieUtils {

  private ResponseCookieUtils() {}

  public static ResponseCookie refreshTokenCookie(String refreshToken) {
    return ResponseCookie.from(REFRESH_TOKEN_COOKIE_KEY)
        .value(refreshToken)
        .httpOnly(true)
        .secure(true)
        .domain(".ourfit.life")
        .sameSite(SameSiteCookies.NONE.getValue())
        .maxAge(REFRESH_TOKEN_EXPIRATION)
        .path("/v1/auth")
        .build();
  }
}
