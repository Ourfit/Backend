package io.ourfit.api.global.filter;

import io.ourfit.api.global.filter.impl.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityFilterFactory {

  private final AuthenticationProvider authenticationProvider;

  public JwtAuthenticationFilter jwtAuth() {
    return new JwtAuthenticationFilter(this.authenticationProvider);
  }
}
