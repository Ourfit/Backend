package io.ourfit.api.domain.terms.service.impl;

import io.ourfit.api.domain.terms.data.dto.internal.TermsRevisionCompactHistoryDto;
import io.ourfit.api.domain.terms.data.entity.TermsRevisionHistory;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import io.ourfit.api.domain.terms.service.TermsRevisionHistoryService;
import io.ourfit.api.infra.persistence.terms.TermsRevisionHistoryQRepository;
import io.ourfit.api.infra.persistence.terms.TermsRevisionHistoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TermsRevisionHistoryServiceImpl implements TermsRevisionHistoryService {

  private final TermsRevisionHistoryRepository revisionHistoryRepository;
  private final TermsRevisionHistoryQRepository revisionHistoryQRepository;

  @Override
  public void save(TermsRevisionHistory revisionHistory) {
    this.revisionHistoryRepository.save(revisionHistory);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType) {
    return this.revisionHistoryQRepository.findCompactHistoriesByType(termsType);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<TermsRevisionHistory> findByTypeAndVersion(
      TermsType termsType, BigDecimal version) {
    return this.revisionHistoryRepository.findByTypeAndVersion(termsType, version);
  }
}
