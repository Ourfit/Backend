package io.ourfit.api.infra.client.http;

import io.ourfit.api.domain.reference.data.dto.internal.KakaoAddressSearchDto;
import io.ourfit.api.domain.reference.data.dto.internal.KakaoKeywordSearchDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakaoLocalClient", url = "https://dapi.kakao.com/v2/local/search")
public interface KakaoLocalClient {

  @GetMapping("/keyword.json")
  KakaoKeywordSearchDto searchByKeyword(
      @RequestParam("query") String query,
      @RequestParam("x") double latitude,
      @RequestParam("y") double longitude);

  @GetMapping("/address.json")
  KakaoAddressSearchDto searchByAddress(@RequestParam("query") String query);
}
