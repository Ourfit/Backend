package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.mate.data.entity.Mate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRepository extends JpaRepository<Mate, Long> {}
