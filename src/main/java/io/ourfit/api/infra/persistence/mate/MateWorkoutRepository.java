package io.ourfit.api.infra.persistence.mate;

import io.ourfit.api.domain.mate.data.entity.MateWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateWorkoutRepository extends JpaRepository<MateWorkout, Long> {}
