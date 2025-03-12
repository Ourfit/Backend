package io.ourfit.api.global.utils;

import java.security.SecureRandom;
import java.util.Random;

public final class RandomGenerator {

  private static final Random RANDOM = new SecureRandom();
  private static final String DEFAULT_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

  private RandomGenerator() {}

  /**
   * 주어진 길이의 무작위 코드를 생성한다.
   *
   * @param requiredLength 생성할 코드의 길이
   * @param characters 코드 생성에 사용할 문자열 템플릿
   * @return 생성된 코드
   */
  public static String nextCode(final int requiredLength, String characters) {
    if (characters.length() < requiredLength) {
      throw new IllegalArgumentException(
          "Character template is too short for the requested code length.");
    }
    StringBuilder sb = new StringBuilder(requiredLength);
    for (int i = 0; i < requiredLength; i++) {
      sb.append(characters.charAt(RANDOM.nextInt(characters.length())));
    }
    return sb.toString();
  }

  /**
   * 주어진 길이의 무작위 코드를 생성한다. <br>
   * 기준 문자열: {@code ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789}
   *
   * @param requiredLength 생성할 코드의 길이
   * @return 생성된 코드
   */
  public static String nextCode(final int requiredLength) {
    StringBuilder sb = new StringBuilder(requiredLength);
    for (int i = 0; i < requiredLength; i++) {
      sb.append(DEFAULT_CHARACTERS.charAt(RANDOM.nextInt(DEFAULT_CHARACTERS.length())));
    }
    return sb.toString();
  }

  /**
   * 주어진 길이의 무작위 바이트 배열을 생성한다.
   *
   * @param requiredLength 생성할 바이트 배열의 길이
   * @return 생성된 바이트 배열
   */
  public static byte[] nextBytes(final int requiredLength) {
    byte[] bytes = new byte[requiredLength];
    RANDOM.nextBytes(bytes);
    return bytes;
  }

  /**
   * 주어진 최소값(min)과 최대값(max) 사이에서 무작위 정수를 생성한다.
   *
   * @param start 생성할 정수의 최소 값 (포함)
   * @param end 생성할 정수의 최대 값 (포함)
   * @return min 이상 max 이하의 무작위 정수
   * @throws IllegalArgumentException min이 max보다 크거나 같을 경우
   */
  public static int nextInt(final int start, final int end) {
    if (start > end) {
      throw new IllegalArgumentException("start range cannot be greater than end range.");
    }
    return start + RANDOM.nextInt(end - start + 1);
  }
}
