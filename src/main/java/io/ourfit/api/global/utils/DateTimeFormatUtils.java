package io.ourfit.api.global.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeFormatUtils {

  private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Seoul");
  private static final ZoneId GMT_ZONE = ZoneId.of("GMT");
  private static final DateTimeFormatter RFC_1123_FORMAT = DateTimeFormatter.RFC_1123_DATE_TIME;

  private DateTimeFormatUtils() {}

  public static String toRFC1123String(LocalDateTime localDateTime) {
    return localDateTime.atZone(GMT_ZONE).format(RFC_1123_FORMAT);
  }

  public static LocalDateTime toLocalDateTime(String rfc1123String) {
    return ZonedDateTime.parse(rfc1123String, RFC_1123_FORMAT)
        .withZoneSameInstant(DEFAULT_ZONE)
        .toLocalDateTime();
  }
}
