package io.ourfit.api.global.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

public final class HttpRequestUtils {

  private static final String BEARER_PREFIX = "Bearer ";

  private HttpRequestUtils() {}

  public static String extractAuthorization(HttpServletRequest request) {
    final var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (!StringUtils.hasText(authorization) || !authorization.startsWith(BEARER_PREFIX)) {
      return null;
    }

    return authorization.substring(BEARER_PREFIX.length());
  }

  public static String resolveClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Real-IP");
    if (ip != null && !ip.isEmpty()) {
      return ip;
    }
    ip = request.getHeader("X-Forwarded-For");
    if (ip != null && !ip.isEmpty()) {
      // 여러 개가 있는 경우 첫 번째 값이 실제 사용자의 IP
      return ip.split(",")[0].trim();
    }
    return request.getRemoteAddr();
  }
}
