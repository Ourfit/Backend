package io.ourfit.api.infra.client.http.config;

import feign.RequestInterceptor;
import io.ourfit.api.domain.auth.data.OAuth2Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;

public class KakaoLocalClientConfig {

  public static final String TARGET_PLACE_CATEGORY_DEPTH_1 = "스포츠,레저";
  public static final String TARGET_PLACE_CATEGORY_DEPTH_2 = "스포츠시설";
  public static final String CATEGORY_NAME_DELIMITER = ">";

  @Bean
  public RequestInterceptor kakaoLocalRequestInterceptor(OAuth2Properties oAuth2Properties) {
    return request ->
        request.header(HttpHeaders.AUTHORIZATION, oAuth2Properties.kakao().getApiKey());
  }
}
