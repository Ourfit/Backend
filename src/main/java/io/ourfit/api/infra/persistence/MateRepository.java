package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.workout.Mate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateRepository extends JpaRepository<Mate, Long> {}
