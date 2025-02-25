package io.ourfit.api.global.security.authentication;

import io.jsonwebtoken.Claims;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.security.userdetails.OurfitUserDetailsImpl;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final UserQueryService userQueryService;
  private final JwtProvider jwtProvider;

  @Override
  public boolean supports(Class<?> authentication) {
    return JwtAuthenticationToken.class.isAssignableFrom(authentication);
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    return Optional.of(authentication)
        .map(JwtAuthenticationToken.class::cast)
        .map(JwtAuthenticationToken::getCredentials)
        .map(Object::toString)
        .flatMap(this.jwtProvider::parse)
        .filter(this::isExpired)
        .map(Claims::getSubject)
        .map(Long::parseLong)
        .flatMap(this.userQueryService::findById)
        .filter(User::isEnabled)
        .map(OurfitUserDetailsImpl::from)
        .map(JwtAuthenticationToken::authenticated)
        .orElseThrow(() -> new BadCredentialsException("Failed to authenticate token"));
  }

  private boolean isExpired(Claims claims) {
    Instant expirationTime = claims.getExpiration().toInstant();
    return Instant.now().isAfter(expirationTime);
  }
}
