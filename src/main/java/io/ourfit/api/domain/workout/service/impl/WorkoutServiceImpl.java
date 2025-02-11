package io.ourfit.api.domain.workout.service.impl;

import io.ourfit.api.domain.workout.data.entity.Workout;
import io.ourfit.api.domain.workout.service.WorkoutService;
import io.ourfit.api.infra.persistence.workout.WorkoutRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

  private final WorkoutRepository repository;

  @Override
  @Cacheable(value = "WORKOUT_TYPES", key = "0", unless = "#result.isEmpty()")
  @Transactional(readOnly = true)
  public List<Workout> findAll() {
    return this.repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Set<Workout> findAllByCodeIn(Set<String> codes) {
    return this.repository.findAllByCodeIn(codes);
  }
}
