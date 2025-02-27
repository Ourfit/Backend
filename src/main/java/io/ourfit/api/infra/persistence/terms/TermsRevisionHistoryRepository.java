package io.ourfit.api.infra.persistence.terms;

import io.ourfit.api.domain.terms.data.entity.TermsRevisionHistory;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRevisionHistoryRepository extends JpaRepository<TermsRevisionHistory, Long> {

  Optional<TermsRevisionHistory> findByTypeAndVersion(TermsType termsType, Double version);
}
