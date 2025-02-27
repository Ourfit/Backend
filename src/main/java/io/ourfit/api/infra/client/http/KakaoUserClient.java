package io.ourfit.api.infra.client.http;

import io.ourfit.api.domain.auth.data.dto.internal.KakaoUserInfo;
import io.ourfit.api.domain.user.data.dto.misc.KakaoUserTermsAgreementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoUserClient", url = "https://kapi.kakao.com")
public interface KakaoUserClient {

  /**
   * 사용자 정보 조회
   *
   * @param accessToken 사용자의 OAuth2 접근 토큰
   * @return 사용자 정보
   */
  @GetMapping("/v2/user/me")
  KakaoUserInfo getUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);

  /**
   * 서비스 약관 동의 정보 조회
   *
   * @param accessToken 사용자의 OAuth2 접근 토큰
   * @param targetIdType 사용자 ID 타입 ({@code TARET_ID_TYPE} 로 고정)
   * @param targetId 사용자 ID
   * @return 서비스 약관 동의 정보
   */
  @GetMapping("/v2/user/service_terms")
  KakaoUserTermsAgreementInfo getTermsAgreementInfo(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
      @RequestParam("target_id_type") String targetIdType,
      @RequestParam("target_id") Long targetId);

  /**
   * 사용자 연결 해제
   *
   * @param accessToken 사용자의 OAuth2 접근 토큰
   */
  @PostMapping("/v1/user/unlink")
  void withdrawal(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken);
}
