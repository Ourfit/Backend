package io.ourfit.api.infra.client.http;

import io.ourfit.api.domain.reference.data.dto.internal.AddressCoordinates;
import io.ourfit.api.domain.reference.data.dto.internal.Places;
import io.ourfit.api.infra.client.http.config.KakaoLocalClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "kakaoLocalClient",
    url = "https://dapi.kakao.com/v2/local/search",
    configuration = KakaoLocalClientConfig.class)
public interface KakaoLocalClient {

  @GetMapping("/keyword.json")
  Places searchByKeyword(
      @RequestParam("query") String query,
      @RequestParam("x") double latitude,
      @RequestParam("y") double longitude);

  @GetMapping("/address.json")
  AddressCoordinates searchByAddress(@RequestParam("query") String query);
}
