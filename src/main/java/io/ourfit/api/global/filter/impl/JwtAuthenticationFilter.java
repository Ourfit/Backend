package io.ourfit.api.global.filter.impl;

import io.ourfit.api.global.filter.AbstractSecurityFilter;
import io.ourfit.api.global.jwt.impl.JwtUtils;
import io.ourfit.api.global.security.authentication.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AbstractSecurityFilter {

  private final AuthenticationProvider authenticationProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (this.shouldNotFilter(request)) {
      this.proceed(request, response, filterChain);
      return;
    }

    Optional<String> userToken = Optional.of(request).map(JwtUtils::resolve);

    if (userToken.isEmpty()) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No Authorization Header");
      return;
    }

    this.setAuthentication(userToken.get());
    this.proceed(request, response, filterChain);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return this.isPublicApiAuthorized(request);
  }

  private void setAuthentication(String token) {
    Authentication authentication =
        this.authenticationProvider.authenticate(new JwtAuthenticationToken(token));

    SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    securityContext.setAuthentication(authentication);
    SecurityContextHolder.setContext(securityContext);
  }
}
