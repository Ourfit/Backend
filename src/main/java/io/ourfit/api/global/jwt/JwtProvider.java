package io.ourfit.api.global.jwt;

import io.jsonwebtoken.Claims;
import io.ourfit.api.domain.user.data.entity.User;
import io.ourfit.api.global.jwt.impl.JwtProperties;
import java.util.Optional;

/** JWT 토큰을 생성하고 검증하는 인터페이스 */
public interface JwtProvider {

  /**
   * {@link User} 정보를 기반으로 토큰을 생성한다.
   *
   * @param user 사용자 정보
   * @return 토큰 발급 응답 DTO
   * @apiNote 갱신 토큰은 캐시 저장소에 저장됩니다.
   */
  OurfitToken create(User user);

  /**
   * 토큰을 재발급한다.
   *
   * @param oldAccessToken 이전 접근 토큰
   * @param refreshToken 갱신 토큰
   * @return 갱신된 토큰 발급 응답 DTO
   * @apiNote 갱신 토큰 유효 기간이 {@link JwtProperties#REFRESH_TOKEN_RENEWAL_THRESHOLD} 이하로 남아있을 경우 갱신 토큰도
   *     함께 갱신됩니다.
   * @throws TokenException 이전 접근 토큰 또는 갱신 토큰이 유효하지 않은 경우
   */
  OurfitToken reissue(String oldAccessToken, String refreshToken);

  /**
   * 토큰을 파싱하여 사용자 정보를 반환한다.
   *
   * @param token 접근 토큰
   * @return 사용자 정보, 변조/만료되었거나 유효하지 않은 토큰인 경우 {@link Optional#empty()}
   */
  Optional<Claims> parse(String token);

  /**
   * 사용자의 갱신 토큰을 폐기한다.
   *
   * @param user 사용자 정보
   */
  void revoke(User user);
}
