package io.ourfit.api.domain.auth.data;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * OAuth2 Client Properties 관리 클래스
 *
 * @param kakao Kakao OAuth2 관련 설정
 */
@ConfigurationProperties(prefix = "security.oauth2.client")
public record OAuth2Properties(Kakao kakao) {

  /**
   * Kakao OAuth2 Properties
   *
   * @param adminKey Kakao Admin Key
   * @param clientId Kakao Client ID
   * @param clientSecret Kakao Client Secret
   * @param redirectUri Kakao Redirect URI
   */
  public record Kakao(String adminKey, String clientId, String clientSecret, String redirectUri) {

    public static final String ADMIN_KEY_PREFIX = "KakaoAK ";

    public static final String GRANT_TYPE = "authorization_code";

    public static final String TARGET_ID_TYPE = "user_id";
  }
}
