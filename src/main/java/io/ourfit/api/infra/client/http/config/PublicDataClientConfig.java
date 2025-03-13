package io.ourfit.api.infra.client.http.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class PublicDataClientConfig {

  @Bean
  public RequestInterceptor kakaoLocalRequestInterceptor(
      @Value("${service.public-data.key}") String servicKey) {
    return request -> request.query("serviceKey", servicKey);
  }
}
