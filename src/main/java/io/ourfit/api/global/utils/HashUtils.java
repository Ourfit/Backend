package io.ourfit.api.global.utils;

import io.ourfit.api.global.exception.custom.InternalProcessingException;
import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.util.Assert;

/** 여러 해시 알고리즘으로 문자열 값을 해싱할 수 있는 유틸리티 클래스 */
public final class HashUtils {

  private static final Base64.Encoder ENCODER = Base64.getEncoder();

  private HashUtils() {}

  /**
   * 지정한 해시 알고리즘으로 문자열 값들을 해싱하고, Base64 인코딩해 반환한다.
   *
   * @param algorithm 사용할 해시 알고리즘 (e.g., {@code SHA-256}, {@code MD5}...)
   * @param values 해싱할 문자열 값
   * @return Base64로 인코딩된 해시 문자열
   */
  public static String hash(@NotNull String algorithm, @NotNull String... values) {
    Assert.notNull(algorithm, "Hash algorithm must not be null");
    Assert.noNullElements(values, "Input values must contain non-null elements");
    try {
      MessageDigest digest = MessageDigest.getInstance(algorithm);
      for (String value : values) {
        digest.update(value.getBytes(StandardCharsets.UTF_8));
      }
      return ENCODER.encodeToString(digest.digest());
    } catch (NoSuchAlgorithmException e) {
      throw new InternalProcessingException(e);
    }
  }
}
