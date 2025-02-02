package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.workout.MateWorkout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MateWorkoutRepository extends JpaRepository<MateWorkout, Long> {}
