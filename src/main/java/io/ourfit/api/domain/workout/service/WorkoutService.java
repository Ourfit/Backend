package io.ourfit.api.domain.workout.service;

import io.ourfit.api.domain.workout.data.entity.Workout;
import java.util.List;
import java.util.Set;

public interface WorkoutService {

  List<Workout> findAll();

  Set<Workout> findAllByCodeIn(Set<String> codes);
}
