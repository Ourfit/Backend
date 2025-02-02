package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.workout.Workout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {}
