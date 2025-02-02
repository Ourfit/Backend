package io.ourfit.api.global.security.authentication;

import io.jsonwebtoken.Claims;
import io.ourfit.api.domain.user.service.UserService;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.security.userdetails.impl.OurfitUserDetailsImpl;
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

  private final UserService userService;
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
        .map(Claims::getSubject)
        .flatMap(this.userService::findByEmail)
        .map(OurfitUserDetailsImpl::from)
        .map(JwtAuthenticationToken::authenticated)
        .orElseThrow(() -> new BadCredentialsException("Invalid token"));
  }
}
