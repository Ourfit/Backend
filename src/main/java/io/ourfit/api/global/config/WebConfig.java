package io.ourfit.api.global.config;

import io.ourfit.api.global.web.resolver.CursorableArgumentResolver;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new AuthenticationPrincipalArgumentResolver());
    resolvers.add(new CursorableArgumentResolver());
  }
}
