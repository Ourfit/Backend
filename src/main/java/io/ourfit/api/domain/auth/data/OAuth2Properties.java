package io.ourfit.api.domain.auth.data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.util.UriTemplate;

/**
 * OAuth2 Client Properties 관리 클래스
 *
 * @param kakao Kakao OAuth2 관련 설정
 */
@ConfigurationProperties(prefix = "security.oauth2.client")
public record OAuth2Properties(String url, Kakao kakao) {

  /**
   * OAuth2 인증 후 사용자를 클라이언트로 리다이렉트하기 위한 URI <br>
   * 다음 프로세스를 위한 정보를 포함한다.
   *
   * <ul>
   *   <li>oAuthId: OAuth2 제공자가 요청 사용자에게 부여한 고유 ID
   *   <li>code: Ourfit 서비스 토큰 발급을 위한 인가 코드
   *   <li>status: 사용자의 등록 상태({@code registred}: 가입한 사용자 / {@code new}: 신규 사용자)
   * </ul>
   */
  public static final UriTemplate OAUTH2_REDIRECT_URI =
      new UriTemplate("{clientUrl}?oAuthId={oAuthId}&code={code}&status={status}");

  /**
   * Kakao OAuth2 Properties
   *
   * @param adminKey Kakao Admin Key
   * @param clientId Kakao Client ID
   * @param clientSecret Kakao Client Secret
   * @param redirectUri Kakao Redirect URI
   */
  public record Kakao(
      String adminKey,
      String clientId,
      String clientSecret,
      String redirectUri,
      String devRedirectUri) {

    public static final String ADMIN_KEY_PREFIX = "KakaoAK ";

    public static final String GRANT_TYPE_ISSUE = "authorization_code";

    public static final String GRANT_TYPE_RENEW = "refresh_token";

    public static final String TARGET_ID_TYPE = "user_id";
  }
}
