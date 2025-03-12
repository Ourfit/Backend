package io.ourfit.api.global.config;

import io.ourfit.api.global.config.properties.EnvironmentProfileType;
import java.nio.charset.StandardCharsets;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;

@Configuration
public class AppConfig {

  @Bean
  public EnvironmentProfileType currentProfile(Environment environment) {
    return EnvironmentProfileType.findByValue(environment.getActiveProfiles()[0]);
  }

  @Bean("customMessageSource")
  public MessageSource customMessageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasenames("CustomMessages");
    messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    messageSource.setFallbackToSystemLocale(true);
    messageSource.setUseCodeAsDefaultMessage(true);
    return messageSource;
  }
}
