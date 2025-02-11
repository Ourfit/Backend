package io.ourfit.api.infra.persistence.terms;

import io.ourfit.api.domain.terms.data.entity.Terms;
import io.ourfit.api.domain.terms.data.entity.TermsType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TermsRepository extends JpaRepository<Terms, Long> {

  Optional<Terms> findByType(TermsType type);
}
