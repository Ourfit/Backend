package io.ourfit.api.domain.terms.service.impl;

import io.ourfit.api.domain.terms.data.dto.internal.TermsUpsertDto;
import io.ourfit.api.domain.terms.data.entity.Terms;
import io.ourfit.api.domain.terms.data.entity.TermsRevisionHistory;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import io.ourfit.api.domain.terms.service.TermsRevisionHistoryService;
import io.ourfit.api.domain.terms.service.TermsService;
import io.ourfit.api.infra.persistence.terms.TermsRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

  private final TermsRepository termsRepository;
  private final TermsRevisionHistoryService revisionHistoryService;

  @Override
  public void upsert(TermsUpsertDto upsertDto) {
    Terms terms =
        this.termsRepository
            .findByType(upsertDto.type())
            .map(exsting -> exsting.update(upsertDto))
            .orElse(Terms.create(upsertDto));

    this.termsRepository.save(terms);
    this.revisionHistoryService.save(TermsRevisionHistory.of(terms, upsertDto.revisionNote()));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Terms> findByType(TermsType termsType) {
    return this.termsRepository.findByType(termsType);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Terms> findAll() {
    return this.termsRepository.findAll();
  }
}
