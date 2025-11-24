package io.ourfit.api.global.security.authentication;

import io.jsonwebtoken.Claims;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.domain.user.service.UserQueryService;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.security.userdetails.OurfitUserDetails;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    if (!(authentication instanceof JwtAuthenticationToken)) {
      throw new BadCredentialsException("JwtAuthenticationToken is required.");
    }
    String token = authentication.getCredentials().toString();
    long userId = Long.parseLong(this.validateAndGetClaims(token).getSubject());

    return this.userQueryService
        .findById(userId)
        .filter(User::isEnabled)
        .map(user -> JwtAuthenticationToken.authenticated(OurfitUserDetails.from(user)))
        .orElseThrow(() -> new UsernameNotFoundException("User not found or disabled."));
  }

  private Claims validateAndGetClaims(String token) {
    return this.jwtProvider
        .parse(token)
        .filter(this::isNotExpired)
        .orElseThrow(() -> new BadCredentialsException("Token is expired or invalid."));
  }

  private boolean isNotExpired(Claims claims) {
    Instant expirationTime = claims.getExpiration().toInstant();
    return Instant.now().isBefore(expirationTime);
  }
}
