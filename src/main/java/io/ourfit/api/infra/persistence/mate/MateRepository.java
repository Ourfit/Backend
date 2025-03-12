package io.ourfit.api.infra.persistence.mate;

import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.enums.MateStatusType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MateRepository extends JpaRepository<Mate, Long>, JpaSpecificationExecutor<Mate> {

  Optional<Mate> findByIdAndStatusType(long mateId, MateStatusType status);
}
