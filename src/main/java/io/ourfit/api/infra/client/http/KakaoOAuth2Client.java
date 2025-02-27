package io.ourfit.api.infra.client.http;

import io.ourfit.api.domain.auth.data.entity.KakaoOAuth2TokenDto;
import io.ourfit.api.infra.client.http.config.KakaoOAuth2ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "kakaoOAuth2Client",
    url = "https://kauth.kakao.com",
    configuration = KakaoOAuth2ClientConfig.class)
public interface KakaoOAuth2Client {

  /**
   * Kakao OAuth2 토큰 발급
   *
   * @param grantType {@code authorization_code}로 고정
   * @param clientId Client ID
   * @param clientSecret Client Secret
   * @param redirectUri 인가 요청시 사용된 리다이렉트 URI
   * @param code 인가 요청으로부터 발급된 인가 코드
   * @return Kakao OAuth2 토큰
   */
  @PostMapping("/oauth/token")
  KakaoOAuth2TokenDto issueToken(
      @RequestParam("grant_type") String grantType,
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("redirect_uri") String redirectUri,
      @RequestParam("code") String code);

  /**
   * Kakao OAuth2 토큰 갱신
   *
   * @param grantType {@code refresh_token}으로 고정
   * @param clientId Client ID
   * @param clientSecret Client Secret
   * @param refreshToken 갱신 토큰
   * @return 갱신된 Kakao OAuth2 토큰
   */
  @PostMapping("/oauth/token")
  KakaoOAuth2TokenDto renewToken(
      @RequestParam("grant_type") String grantType,
      @RequestParam("client_id") String clientId,
      @RequestParam("client_secret") String clientSecret,
      @RequestParam("refresh_token") String refreshToken);
}
