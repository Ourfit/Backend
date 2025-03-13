package io.ourfit.api.infra.client.http;

import io.ourfit.api.infra.client.http.config.PublicDataClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** 공공데이터 API 요청 클라이언트 */
@FeignClient(
    name = "publicDataClient",
    url = "https://apis.data.go.kr",
    configuration = PublicDataClientConfig.class)
public interface PublicDataClient {

  @GetMapping("/B090041/openapi/service/SpcdeInfoService/getRestDeInfo")
  String getHoliday(@RequestParam("solYear") String year, @RequestParam("solMonth") String month);
}
