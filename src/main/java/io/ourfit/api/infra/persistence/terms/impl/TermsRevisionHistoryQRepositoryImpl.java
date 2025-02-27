package io.ourfit.api.infra.persistence.terms.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.ourfit.api.domain.terms.data.dto.internal.TermsRevisionCompactHistoryDto;
import io.ourfit.api.domain.terms.data.entity.QTermsRevisionHistory;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import io.ourfit.api.infra.persistence.terms.TermsRevisionHistoryQRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class TermsRevisionHistoryQRepositoryImpl implements TermsRevisionHistoryQRepository {

  private static final QTermsRevisionHistory qTermsRevisionHistory =
      QTermsRevisionHistory.termsRevisionHistory;

  private final JPAQueryFactory queryFactory;

  @Override
  @Transactional(readOnly = true)
  public List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType) {
    return this.queryFactory
        .select(
            Projections.constructor(
                TermsRevisionCompactHistoryDto.class,
                qTermsRevisionHistory.version,
                qTermsRevisionHistory.revisionNote,
                qTermsRevisionHistory.createdAt))
        .from(qTermsRevisionHistory)
        .where(qTermsRevisionHistory.type.eq(termsType))
        .fetch();
  }
}
