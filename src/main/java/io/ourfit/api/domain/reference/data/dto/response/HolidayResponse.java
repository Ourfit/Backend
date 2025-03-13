package io.ourfit.api.domain.reference.data.dto.response;

import io.ourfit.api.domain.reference.data.dto.internal.Holidays;

/**
 * 공휴일 정보 응답 DTO
 *
 * @param name 이름 (e.g., 어린이날)
 * @param date 날짜 (yyyy-MM-dd)
 */
public record HolidayResponse(String name, String date) {

  public static HolidayResponse from(Holidays.Item holiday) {
    return new HolidayResponse(holiday.dateName(), holiday.date());
  }
}
