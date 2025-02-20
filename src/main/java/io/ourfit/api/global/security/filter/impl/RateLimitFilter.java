package io.ourfit.api.global.security.filter.impl;

import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.global.security.data.OurfitAuditorAware;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.security.data.annotation.RateLimit;
import io.ourfit.api.global.security.filter.AbstractSecurityFilter;
import io.ourfit.api.global.security.support.RateLimiter;
import io.ourfit.api.global.web.resolver.HandlerMethodAnnotationResolver;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@RequiredArgsConstructor
public class RateLimitFilter extends AbstractSecurityFilter {

  private static final String RATE_LIMIT_PREFIX = "RATE::LIMIT";
  private static final String KEY_DELIMITER = "::";

  private final HandlerMethodAnnotationResolver annotationResolver;
  private final OurfitAuditorAware auditorAware;
  private final RateLimiter rateLimiter;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    Optional<RateLimit> optionalRateLimit = this.annotationResolver.find(request, RateLimit.class);
    Optional<PublicApi> optionalPublicApi = this.annotationResolver.find(request, PublicApi.class);

    if (optionalRateLimit.isEmpty()) {
      this.proceed(request, response, filterChain);
      return;
    }

    RateLimit rateLimit = optionalRateLimit.get();
    Assert.isTrue(rateLimit.maxRequests() > 0, "maxRequests must be greater than 0");
    Assert.isTrue(rateLimit.duration() > 0, "duration must be greater than 0");
    if (rateLimit.limitType() == RateLimit.LimitType.USER && optionalPublicApi.isPresent()) {
      throw new IllegalStateException(
          "RateLimit annotation cannot be used with PublicApi annotation");
    }

    String key = this.generateRateLimitKey(request, rateLimit);
    boolean isAllowed = this.rateLimiter.tryConsume(key, rateLimit);

    if (needsRateLimitHandling(!isAllowed, rateLimit)) {
      this.doHandle(response, rateLimit);
      return;
    }

    this.proceed(request, response, filterChain);
  }

  private String generateRateLimitKey(HttpServletRequest request, RateLimit rateLimit) {
    var uri = request.getRequestURI();
    var method = request.getMethod();
    return switch (rateLimit.limitType()) {
      case IP ->
          String.join(KEY_DELIMITER, RATE_LIMIT_PREFIX, request.getRemoteAddr(), method, uri);
      case USER -> {
        long userId =
            this.auditorAware.getCurrentAuditor().orElseThrow(AuthenticationException::new);
        yield String.join(KEY_DELIMITER, RATE_LIMIT_PREFIX, String.valueOf(userId), method, uri);
      }
    };
  }

  private void doHandle(HttpServletResponse response, RateLimit rateLimit) throws IOException {
    if (rateLimit.includeRetryAfterHeader()) {
      long retryAfterInSeconds =
          Duration.of(rateLimit.duration(), rateLimit.durationUnit()).toSeconds();
      response.setHeader(HttpHeaders.RETRY_AFTER, String.valueOf(retryAfterInSeconds));
    }
    response.sendError(HttpStatus.TOO_MANY_REQUESTS.value());
  }

  private static boolean needsRateLimitHandling(final boolean isExceeded, RateLimit rateLimit) {
    return switch (rateLimit.exceedAction()) {
      case BLOCK -> isExceeded;
      case THROTTLE -> false;
    };
  }
}
