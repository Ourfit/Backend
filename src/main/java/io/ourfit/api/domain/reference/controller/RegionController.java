package io.ourfit.api.domain.reference.controller;

import io.ourfit.api.domain.reference.data.dto.response.RegionResponse;
import io.ourfit.api.domain.reference.service.RegionService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.security.data.annotation.PublicApi;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.global.utils.StringUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/regions")
public class RegionController {

  private static final int MAX_KEYWORD_LENGTH = 20;
  private static final int MIN_KEYWORD_LENGTH = 2;

  private final RegionService service;

  @PublicApi
  @GetMapping
  public ResponseEntity<BaseResponse<List<RegionResponse>>> findAllByKeyword(
      @RequestParam("q") String keyword) {
    final var sanitizedKeyword =
        StringUtils.normalizeKoreanKeyword(keyword, MAX_KEYWORD_LENGTH, MIN_KEYWORD_LENGTH);

    if (sanitizedKeyword.isBlank()) {
      return ResponseEntity.ok().build();
    }

    var contents =
        StreamUtils.mapToList(this.service.findAllByKeyword(keyword), RegionResponse::from);
    return ResponseEntity.ok(BaseResponse.from(contents));
  }
}
