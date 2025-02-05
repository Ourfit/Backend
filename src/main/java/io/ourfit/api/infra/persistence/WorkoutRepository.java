package io.ourfit.api.infra.persistence;

import io.ourfit.api.domain.workout.data.entity.Workout;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {

  Set<Workout> findAllByCodeIn(Set<String> codes);
}
