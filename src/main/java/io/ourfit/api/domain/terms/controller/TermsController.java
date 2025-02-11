package io.ourfit.api.domain.terms.controller;

import io.ourfit.api.domain.terms.data.dto.response.TermsResponse;
import io.ourfit.api.domain.terms.data.dto.response.TermsRevisionCompactHistoryResponse;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import io.ourfit.api.domain.terms.service.TermsRevisionHistoryService;
import io.ourfit.api.domain.terms.service.TermsService;
import io.ourfit.api.global.data.dto.BaseResponse;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.utils.StreamUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/terms")
public class TermsController {

  private final TermsService termsService;
  private final TermsRevisionHistoryService revisionHistoryService;

  //  @AdminApi(superAdminOnly = true, audit = true)
  //  @PostMapping
  //  public ResponseEntity<BaseResponse<Void>> upsert(
  //      @RequestBody @Valid final TermsUpsertRequest request) {
  //    this.termsService.upsert(TermsUpsertDto.fromRequest(request));
  //    return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.empty());
  //  }

  @GetMapping
  public ResponseEntity<BaseResponse<List<TermsResponse>>> findAll() {
    List<TermsResponse> response =
        StreamUtils.mapToList(this.termsService.findAll(), TermsResponse::toCompact);
    return ResponseEntity.ok(BaseResponse.from(response));
  }

  @GetMapping("/{type}")
  public ResponseEntity<BaseResponse<TermsResponse>> findByType(@PathVariable final String type) {
    TermsResponse response =
        this.termsService
            .findByType(TermsType.findByName(type))
            .map(TermsResponse::toDetailed)
            .orElseThrow(NoSuchEntityException::new);
    return ResponseEntity.ok(BaseResponse.from(response));
  }

  @GetMapping("/{type}/revisions")
  public ResponseEntity<BaseResponse<List<TermsRevisionCompactHistoryResponse>>>
      findRevisionHistoriesByType(@PathVariable final String type) {
    List<TermsRevisionCompactHistoryResponse> response =
        StreamUtils.mapToList(
            this.revisionHistoryService.findCompactHistoriesByType(TermsType.findByName(type)),
            TermsRevisionCompactHistoryResponse::from);
    return ResponseEntity.ok(BaseResponse.from(response));
  }

  @GetMapping("/{type}/revisions/{version}")
  public ResponseEntity<BaseResponse<TermsResponse>> findRevisionByTypeAndVersion(
      @PathVariable final String type, @PathVariable final Double version) {
    TermsResponse response =
        this.revisionHistoryService
            .findByTypeAndVersion(TermsType.findByName(type), version)
            .map(TermsResponse::toDetailed)
            .orElseThrow(NoSuchEntityException::new);

    return ResponseEntity.ok(BaseResponse.from(response));
  }
}
