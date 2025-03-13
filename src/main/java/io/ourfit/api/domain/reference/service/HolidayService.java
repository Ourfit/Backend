package io.ourfit.api.domain.reference.service;

import io.ourfit.api.domain.reference.data.dto.internal.Holidays;
import java.time.YearMonth;

public interface HolidayService {

  /**
   * 공휴일 정보를 조회한다.
   *
   * @param yearMonth 조회할 연월(yyyy-MM)
   * @return 조회된 공휴일 정보
   */
  Holidays fetchHolidays(YearMonth yearMonth);
}
