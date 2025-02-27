package io.ourfit.api.infra.persistence.terms;

import io.ourfit.api.domain.terms.data.dto.internal.TermsRevisionCompactHistoryDto;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import java.util.List;

public interface TermsRevisionHistoryQRepository {

  List<TermsRevisionCompactHistoryDto> findCompactHistoriesByType(TermsType termsType);
}
