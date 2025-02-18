package io.ourfit.api.infra.persistence.mate;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRepository extends JpaRepository<Mate, Long> {

  Optional<Mate> findByIdAndStatusType(long mateId, MateStatusType status);
}
