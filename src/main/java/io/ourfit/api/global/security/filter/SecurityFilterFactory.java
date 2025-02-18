package io.ourfit.api.global.security.filter;

import io.ourfit.api.global.security.data.OurfitAuditorAware;
import io.ourfit.api.global.security.filter.impl.AdminApiAuthorizationFilter;
import io.ourfit.api.global.security.filter.impl.JwtAuthenticationFilter;
import io.ourfit.api.global.security.filter.impl.PublicApiAccessControlFilter;
import io.ourfit.api.global.web.resolver.HandlerMethodAnnotationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityFilterFactory {

  private final AuthenticationProvider authenticationProvider;
  private final HandlerMethodAnnotationResolver annotationResolver;
  private final Environment environment;
  private final OurfitAuditorAware auditorAware;
  private final ApplicationEventPublisher eventPublisher;

  public JwtAuthenticationFilter jwtAuth() {
    return new JwtAuthenticationFilter(this.authenticationProvider);
  }

  public PublicApiAccessControlFilter publicAccess() {
    return new PublicApiAccessControlFilter(
        this.environment.getRequiredProperty("service.key.api"), this.annotationResolver);
  }

  public AdminApiAuthorizationFilter adminAuth() {
    return new AdminApiAuthorizationFilter(
        this.annotationResolver, this.auditorAware, this.eventPublisher);
  }
}
