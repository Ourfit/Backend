package io.ourfit.api.global.security.filter;

import io.ourfit.api.global.security.filter.impl.JwtAuthenticationFilter;
import io.ourfit.api.global.security.filter.impl.PublicApiAccessControlFilter;
import io.ourfit.api.global.web.resolver.HandlerMethodAnnotationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityFilterFactory {

  private final AuthenticationProvider authenticationProvider;
  private final HandlerMethodAnnotationResolver annotationResolver;

  public JwtAuthenticationFilter jwtAuth() {
    return new JwtAuthenticationFilter(this.authenticationProvider);
  }

  public PublicApiAccessControlFilter publicAccess() {
    return new PublicApiAccessControlFilter("apiKey", this.annotationResolver);
  }
}
