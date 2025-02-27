package io.ourfit.api.domain.auth.service;

import io.ourfit.api.domain.auth.data.dto.internal.OAuth2UserInfo;
import io.ourfit.api.global.exception.custom.AuthenticationException;

/** OAuth2 인증 서비스 */
public interface OAuth2Service {

  /**
   * OAuth2 ID로 사용자 정보를 조회한다.
   *
   * @param oAuthId 조회할 사용자의 OAuth ID
   * @return OAuth2 사용자 정보
   * @throws AuthenticationException OAuth2 인증 정보가 없거나 유효하지 않은 경우
   */
  OAuth2UserInfo getUserInfo(String oAuthId);

  /**
   * 사용자를 OAuth2 탈퇴 처리한다.
   *
   * @param oAuthId 탈퇴 처리할 사용자의 OAuth ID
   * @throws AuthenticationException OAuth2 인증 정보가 없거나 유효하지 않은 경우
   */
  void withdrawal(String oAuthId);
}
