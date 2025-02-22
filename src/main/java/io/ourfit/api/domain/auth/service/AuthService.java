package io.ourfit.api.domain.auth.service;

import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.jwt.OurfitToken;

public interface AuthService {

  /**
   * 토큰을 발급한다.
   *
   * @param oAuthId 토큰을 발급할 사용자의 OAuth ID
   * @return 발급된 토큰
   * @throws AuthenticationException {@code oAuthId}에 매칭되는 인증 정보를 찾을 수 없는 경우
   * @throws NoSuchEntityException {@code oAuthId}에 매칭되는 사용자 정보를 찾을 수 없는 경우
   */
  OurfitToken issue(String oAuthId);

  /**
   * 토큰을 재발급한다.
   *
   * @param accessToken 이전 접근 토큰
   * @param refreshToken 갱신 토큰
   * @return 재발급된 토큰
   * @throws AuthenticationException 이전 접근 토큰이 유효하지 않은 경우
   * @apiNote {@link JwtProvider#reissue}와 동일한 기능을 수행한다.
   */
  OurfitToken reissue(String accessToken, String refreshToken);

  /**
   * 사용자의 갱신 토큰을 폐기한다.
   *
   * @param user 토큰 폐기를 요청한 사용자 정보
   * @apiNote {@link JwtProvider#revoke}와 동일한 기능을 수행한다.
   */
  void revoke(User user);
}
