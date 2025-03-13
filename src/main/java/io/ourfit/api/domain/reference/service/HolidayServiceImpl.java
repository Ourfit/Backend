package io.ourfit.api.domain.reference.service;

import io.ourfit.api.domain.reference.data.dto.internal.Holidays;
import io.ourfit.api.global.utils.XmlUtils;
import io.ourfit.api.infra.client.http.PublicDataClient;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

  private final PublicDataClient publicDataClient;

  @Override
  @Cacheable(value = "HOLIDAYS", key = "#yearMonth.toString()")
  public Holidays fetchHolidays(YearMonth yearMonth) {
    var datas =
        this.publicDataClient.getHoliday(
            String.valueOf(yearMonth.getYear()), String.format("%02d", yearMonth.getMonthValue()));
    return XmlUtils.readValue(datas, Holidays.class);
  }
}
