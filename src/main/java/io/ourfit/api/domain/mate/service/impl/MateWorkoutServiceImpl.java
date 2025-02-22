package io.ourfit.api.domain.mate.service.impl;

import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutPlacesUpsertDto;
import io.ourfit.api.domain.mate.data.dto.internal.MateWorkoutTimeUpsertDto;
import io.ourfit.api.domain.mate.data.entity.Mate;
import io.ourfit.api.domain.mate.data.entity.MateWorkout;
import io.ourfit.api.domain.mate.service.MateWorkoutService;
import io.ourfit.api.global.exception.ApiExceptionType;
import io.ourfit.api.global.exception.custom.NoSuchEntityException;
import io.ourfit.api.global.utils.StreamUtils;
import io.ourfit.api.infra.persistence.mate.MateWorkoutRepository;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MateWorkoutServiceImpl implements MateWorkoutService {

  private final MateWorkoutRepository repository;

  @Override
  public void initialize(Mate mate) {
    this.repository.save(MateWorkout.ofEmpty(mate));
  }

  @Override
  public void putWorkoutPlaces(final long mateId, MateWorkoutPlacesUpsertDto upsertDto) {
    this.ifFoundThen(mateId, mateWorkout -> mateWorkout.putWorkoutPlaces(upsertDto));
  }

  @Override
  public void putWorkoutTimes(final long mateId, MateWorkoutTimeUpsertDto upsertDto) {
    this.ifFoundThen(mateId, mateWorkout -> mateWorkout.putWorkoutTimes(upsertDto));
  }

  @SafeVarargs
  private void ifFoundThen(
      long id, Consumer<MateWorkout> action, Predicate<MateWorkout>... filters) {
    this.repository
        .findById(id)
        .map(entity -> StreamUtils.applyFiltersOrThrow(entity, filters))
        .ifPresentOrElse(
            action,
            () -> {
              throw new NoSuchEntityException(ApiExceptionType.NOT_FOUND_MATE_WORKOUT);
            });
  }
}
