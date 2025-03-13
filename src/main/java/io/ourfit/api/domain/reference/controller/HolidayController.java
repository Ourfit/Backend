package io.ourfit.api.domain.reference.controller;

import io.ourfit.api.domain.reference.data.dto.response.HolidayResponse;
import io.ourfit.api.domain.reference.service.HolidayService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.ListResponse;
import io.ourfit.api.global.utils.StreamUtils;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/holidays")
public class HolidayController {

  private final HolidayService service;

  @GetMapping("/{yearMonth}")
  public ResponseEntity<ListResponse<HolidayResponse>> fetchHolidays(
      @PathVariable final String yearMonth) {
    var result = this.service.fetchHolidays(YearMonth.parse(yearMonth));
    return ResponseEntity.ok(
        ApiResponse.of(StreamUtils.mapToList(result.items(), HolidayResponse::from)));
  }
}
