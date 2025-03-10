package io.ourfit.api.global.utils;

import static io.ourfit.api.global.data.Versionable.GMT_ZONE;
import static io.ourfit.api.global.data.Versionable.RFC_1123_FORMAT;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateTimeUtils {

  private DateTimeUtils() {}

  /**
   * {@link LocalDateTime}을 RFC 1123 형식의 문자열로 변환한다.
   *
   * @param localDateTime 변환할 {@link LocalDateTime} 객체
   * @return RFC 1123 형식의 문자열
   */
  public static String toRFC1123String(LocalDateTime localDateTime) {
    return localDateTime.atZone(GMT_ZONE).format(RFC_1123_FORMAT);
  }

  /**
   * RFC 1123 형식의 문자열을 {@link LocalDateTime} 객체로 파싱한다.
   *
   * @param rfc1123String RFC 1123 형식의 문자열
   * @return 파싱한 {@link LocalDateTime} 객체
   */
  public static LocalDateTime toLocalDateTime(String rfc1123String) {
    return ZonedDateTime.parse(rfc1123String, RFC_1123_FORMAT)
        .withZoneSameInstant(ZoneId.systemDefault())
        .toLocalDateTime();
  }

  public static boolean isWithinThisWeek(
      LocalDate date, LocalDate startOfWeek, LocalDate endOfWeek) {
    return !date.isBefore(startOfWeek) && !date.isAfter(endOfWeek);
  }
}
