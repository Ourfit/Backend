package io.ourfit.api.global.jwt.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.MacAlgorithm;
import java.security.Key;
import java.time.Duration;
import java.util.Base64;
import javax.crypto.SecretKey;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.http.HttpHeaders;

/**
 * JWT 관련 설정 정보 Properties
 *
 * @param issuer 발급자(issuer) 정보
 * @param secret 서명에 사용할 비밀키
 * @param key 서명에 사용할 {@link Key}
 */
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String issuer, String secret, SecretKey key) {

  /** JWT 인증을 위한 {@link HttpHeaders#AUTHORIZATION} 헤더의 접두사 */
  public static final String BEARER_PREFIX = "Bearer ";

  /** JWT에서 사용자 권한 정보를 저장하는 클레임 키 */
  public static final String AUTHENTICATION_KEY = "auth";

  /** 접근 토큰의 기본 만료 시간 */
  public static final Duration ACCESS_TOKEN_EXPIRATION = Duration.ofHours(2);

  /** 갱신 토큰의 기본 만료 시간 */
  public static final Duration REFRESH_TOKEN_EXPIRATION = Duration.ofDays(14);

  /** 접근 토큰 갱신 시, 갱신 토큰도 자동으로 갱신하는 기준 시간 */
  public static final Duration REFRESH_TOKEN_RENEWAL_THRESHOLD = Duration.ofDays(1);

  /** JWT 서명에 사용할 알고리즘 */
  static final MacAlgorithm SIGNATURE_ALGORITHM = Jwts.SIG.HS512;

  @ConstructorBinding
  public JwtProperties(String issuer, String secret) {
    this(issuer, secret, Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret)));
  }
}
