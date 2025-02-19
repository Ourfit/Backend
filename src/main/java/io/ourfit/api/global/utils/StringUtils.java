package io.ourfit.api.global.utils;

public final class StringUtils {

  private static final String EMPTY_STRING = "";
  private static final char EMPTY_CHAR = ' ';

  private static final char KOREAN_START_CHAR = '가';
  private static final char KOREAN_END_CHAR = '힣';
  private static final char NUMBER_START_CHAR = '0';
  private static final char NUMBER_END_CHAR = '9';

  private StringUtils() {}

  public static String normalizeKoreanKeyword(String input, int maxLength, int minLength) {
    if (input == null || input.isBlank()) {
      return EMPTY_STRING;
    }

    StringBuilder sb = new StringBuilder();
    boolean lastWasSpace = true;

    for (char ch : input.toCharArray()) {
      if (isKoreanOrDigitOrSpace(ch)) {
        if (ch == EMPTY_CHAR && lastWasSpace) {
          continue; // 연속된 공백 제거
        }
        sb.append(ch);
        lastWasSpace = (ch == EMPTY_CHAR);
      }
    }

    String result = sb.toString().trim();
    return (result.length() >= minLength && result.length() <= maxLength) ? result : EMPTY_STRING;
  }

  private static boolean isKoreanOrDigitOrSpace(char ch) {
    return (ch >= KOREAN_START_CHAR && ch <= KOREAN_END_CHAR)
        || (ch >= NUMBER_START_CHAR && ch <= NUMBER_END_CHAR)
        || ch == EMPTY_CHAR;
  }
}
