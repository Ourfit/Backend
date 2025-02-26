package io.ourfit.api.domain.terms.controller;

import io.ourfit.api.domain.terms.data.dto.internal.TermsUpsertDto;
import io.ourfit.api.domain.terms.data.dto.request.TermsUpsertRequest;
import io.ourfit.api.domain.terms.data.dto.response.TermsResponse;
import io.ourfit.api.domain.terms.data.dto.response.TermsRevisionCompactHistoryResponse;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import io.ourfit.api.domain.terms.service.TermsRevisionHistoryService;
import io.ourfit.api.domain.terms.service.TermsService;
import io.ourfit.api.global.data.ApiResponse;
import io.ourfit.api.global.data.dto.ListResponse;
import io.ourfit.api.global.data.dto.SingleResponse;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.security.data.annotation.AdminApi;
import io.ourfit.api.global.utils.StreamUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/terms")
public class TermsController {

  private final TermsService termsService;
  private final TermsRevisionHistoryService revisionHistoryService;

  @AdminApi(superAdminOnly = true, audit = true)
  @PostMapping
  public ResponseEntity<Void> upsert(@RequestBody @Valid final TermsUpsertRequest request) {
    this.termsService.upsert(TermsUpsertDto.fromRequest(request));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping
  public ResponseEntity<ListResponse<TermsResponse>> findAll() {
    List<TermsResponse> response =
        StreamUtils.mapToList(this.termsService.findAll(), TermsResponse::toCompact);
    return ResponseEntity.ok(ApiResponse.of(response));
  }

  @GetMapping("/{type}")
  public ResponseEntity<SingleResponse<TermsResponse>> findByType(@PathVariable final String type) {
    TermsResponse response =
        this.termsService
            .findByType(TermsType.findByName(type))
            .map(TermsResponse::toDetailed)
            .orElseThrow(NoSuchEntityException::new);
    return ResponseEntity.ok(ApiResponse.of(response));
  }

  @GetMapping("/{type}/revisions")
  public ResponseEntity<ListResponse<TermsRevisionCompactHistoryResponse>>
      findRevisionHistoriesByType(@PathVariable final String type) {
    List<TermsRevisionCompactHistoryResponse> response =
        StreamUtils.mapToList(
            this.revisionHistoryService.findCompactHistoriesByType(TermsType.findByName(type)),
            TermsRevisionCompactHistoryResponse::from);
    return ResponseEntity.ok(ApiResponse.of(response));
  }

  @GetMapping("/{type}/revisions/{version}")
  public ResponseEntity<SingleResponse<TermsResponse>> findRevisionByTypeAndVersion(
      @PathVariable final String type, @PathVariable final Double version) {
    TermsResponse response =
        this.revisionHistoryService
            .findByTypeAndVersion(TermsType.findByName(type), version)
            .map(TermsResponse::toDetailed)
            .orElseThrow(NoSuchEntityException::new);

    return ResponseEntity.ok(ApiResponse.of(response));
  }
}
