package io.ourfit.api.domain.auth.service;

import io.ourfit.api.domain.auth.data.entity.OurfitAuthCode;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.exception.custom.AuthenticationException;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.jwt.JwtProvider;
import io.ourfit.api.global.jwt.OurfitToken;

public interface AuthService {

  /**
   * 인가 코드를 발급한다.
   *
   * @param oAuthId 인가 코드를 발급할 사용자의 OAuth2 ID
   * @return 발급된 인가 코드
   */
  OurfitAuthCode issueAuthCode(String oAuthId);

  /**
   * 토큰을 발급한다.
   *
   * @param oAuthId 토큰을 발급할 사용자의 OAuth2 ID
   * @param authCode OAuth2 인증 후 발급된 인가 코드
   * @return 발급된 토큰
   * @throws AuthenticationException {@code oAuthId}에 매칭되는 인증 정보를 찾을 수 없는 경우
   * @throws NoSuchEntityException {@code oAuthId}에 매칭되는 사용자 정보를 찾을 수 없는 경우
   */
  OurfitToken issueToken(String oAuthId, String authCode);

  /**
   * 토큰을 재발급한다.
   *
   * @param accessToken 이전 접근 토큰
   * @param refreshToken 갱신 토큰
   * @return 재발급된 토큰
   * @throws AuthenticationException 이전 접근 토큰이 유효하지 않은 경우
   * @apiNote {@link JwtProvider#reissue}와 동일한 기능을 수행한다.
   */
  OurfitToken reissueToken(String accessToken, String refreshToken);

  /**
   * OAuth2 인증 과정에서 발급된 인가 코드를 검증하고 소비한다.
   *
   * <ul>
   *   <li>저장된 인가 코드(`oAuthId` 기반 조회)를 찾아 검증합니다.
   *   <li>저장된 코드와 입력된 코드(`inputCode`)가 다르면 인증 예외가 발생합니다.
   *   <li>인증에 성공 여부와 관련 없이 해당 인가 코드를 저장소에서 삭제하여 재사용을 방지합니다.
   * </ul>
   *
   * @param oAuthId OAuth2 서비스 제공자가 발급한 사용자 식별자
   * @param inputCode 클라이언트가 전달한 인가 코드
   * @throws AuthenticationException 저장된 코드가 없거나 입력 코드가 일치하지 않을 경우
   */
  void consumeAuthCode(final String oAuthId, final String inputCode);

  /**
   * 사용자의 갱신 토큰을 폐기한다.
   *
   * @param user 토큰 폐기를 요청한 사용자 정보
   * @apiNote {@link JwtProvider#revoke}와 동일한 기능을 수행한다.
   */
  void revokeToken(User user);
}
